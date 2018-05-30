package com.alexzfx.earlywarninguser.service.impl;

import com.alexzfx.earlywarninguser.entity.*;
import com.alexzfx.earlywarninguser.entity.e.MaintainStatus;
import com.alexzfx.earlywarninguser.exception.BaseException;
import com.alexzfx.earlywarninguser.repository.InstOrderRepository;
import com.alexzfx.earlywarninguser.repository.MessageRepository;
import com.alexzfx.earlywarninguser.repository.RoleRepository;
import com.alexzfx.earlywarninguser.repository.UserRepository;
import com.alexzfx.earlywarninguser.service.MsgPushService;
import com.alexzfx.earlywarninguser.util.WSUtil.SocketSessionRegistry;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

/**
 * Author : Alex
 * Date : 2018/4/17 14:22
 * Description : 用于消息推送并保存
 */
@Service
@Transactional
public class MsgPushServiceImpl implements MsgPushService {

    private final SocketSessionRegistry register;

    private final SimpMessagingTemplate template;

//    private final Gson gson;

    private final InstOrderRepository instOrderRepository;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final MessageRepository messageRepository;
    private final BaseException PermissionDenied;
    @Resource(name = "userSocketPath")
    private String userSocketPath;
    @Resource(name = "maintainerSocketPath")
    private String maintainerSocketPath;

    private final BaseException OperationError;


    @Autowired
    public MsgPushServiceImpl(SocketSessionRegistry register, SimpMessagingTemplate template, InstOrderRepository instOrderRepository, UserRepository userRepository, RoleRepository roleRepository, MessageRepository messageRepository, BaseException PermissionDenied, BaseException OperationError) {
        this.register = register;
        this.template = template;
        this.instOrderRepository = instOrderRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.messageRepository = messageRepository;
        this.PermissionDenied = PermissionDenied;
        this.OperationError = OperationError;
    }

    //对用户和维修人员进行任务分配，并对在线的用户进行消息推送
    @Override
    public void pushAndSave(Instrument instrument, MachineData machineData, User maintainer, InstOrder order) {
        Timestamp timestamp = order.getCreateTime();
        String maintainerName = maintainer.getName() == null ? maintainer.getUsername() : maintainer.getName();

        //创建用户的推送消息
        Message userMessage = new Message();
        User user = instrument.getCreater();
        String username = user.getName() == null ? user.getUsername() : user.getName();
        userMessage.setMachineId(instrument.getId());
        userMessage.setData(machineData.getData());
        userMessage.setInstName(instrument.getName());
        userMessage.setThresholdValue(instrument.getThresholdValue());
        userMessage.setCreateTime(timestamp);
        userMessage.setStatus(order.getEnumMaintainStatus());
        userMessage.setUid(user.getId());
        userMessage.setOrderId(order.getId());
        //考虑是否需要抽象出来，但感觉复用性很小
        userMessage.setContent("尊敬的" + username + ",您好。"
                + "您所创建的仪器" + instrument.getName() + "(" + instrument.getId() + ")"
                + "于" + timestamp.getYear() + "年" + (timestamp.getMonth() + 1) + "月" + timestamp.getDate() + "日"
                + timestamp.getHours() + "时" + timestamp.getMinutes() + "分" + timestamp.getSeconds() + "秒"
                + "发生了告警，仪器数值为" + machineData.getData() + "(" + instrument.getThresholdValue() + ")。"
                + "系统已将此任务分配给了工作人员" + maintainerName
                + ",其联系方式为" + maintainer.getEmail() + ",你可以进入xx页面查看详情。");

        //创建维修人员推送消息
        Message maintainerMessage = new Message();
        maintainerMessage.setMachineId(instrument.getId());
        maintainerMessage.setData(machineData.getData());
        maintainerMessage.setThresholdValue(instrument.getThresholdValue());
        maintainerMessage.setCreateTime(timestamp);
        maintainerMessage.setInstName(instrument.getName());
        maintainerMessage.setUid(maintainer.getId());
        maintainerMessage.setStatus(order.getEnumMaintainStatus());
        maintainerMessage.setOrderId(order.getId());
        maintainerMessage.setContent("尊敬的" + maintainerName + ",您好。"
                + "用户" + username + "所创建的仪器" + instrument.getName() + "(" + instrument.getId() + ")"
                + "于" + (1900 + timestamp.getYear()) + "年" + (timestamp.getMonth() + 1) + "月" + timestamp.getDate() + "日"
                + timestamp.getHours() + "时" + timestamp.getMinutes() + "分" + timestamp.getSeconds() + "秒"
                + "发生了告警，仪器数值为" + machineData.getData() + "(" + instrument.getThresholdValue() + ")"
                + "系统已将此任务分配给了您" + "用户联系方式为" + user.getEmail()
                + ",你可以进入xx页面查看详情。请及时处理");

        messageRepository.save(userMessage);
        messageRepository.save(maintainerMessage);
        Set<String> userSessionId = register.getSessionIds(user.getUsername());
        for (String sessionId : userSessionId) {
            template.convertAndSendToUser(sessionId, userSocketPath, JSON.toJSONString(userMessage), createHeaders(sessionId));
        }
        Set<String> maintainerSessionId = register.getSessionIds(maintainer.getUsername());
        for (String sessionId : maintainerSessionId) {
            template.convertAndSendToUser(sessionId, maintainerSocketPath, JSON.toJSONString(maintainerMessage), createHeaders(sessionId));
        }
    }

    //确认订单，传入的是
    @Override
    public void pushConfirmAndSave(List<Long> orderIds) {
        for (Long id : orderIds) {
            InstOrder order = instOrderRepository.getOne(id);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            order.setConfirmTime(timestamp);
            if (order.getEnumMaintainStatus() == MaintainStatus.WAITCONFIRM) {
                order.setMaintainStatus(MaintainStatus.CONFIRMED);
            } else {
                throw OperationError;
            }
            instOrderRepository.save(order);
            User user = userRepository.getOne(order.getOwnerId());
            Instrument instrument = order.getInstrument();
            //创建消息并发送给仪器所有者
            Message userMessage = new Message();
            String username = user.getName() == null ? user.getUsername() : user.getName();
            userMessage.setMachineId(instrument.getId());
            userMessage.setThresholdValue(instrument.getThresholdValue());
            userMessage.setCreateTime(timestamp);
            userMessage.setInstName(instrument.getName());
            userMessage.setStatus(order.getEnumMaintainStatus());
            userMessage.setUid(user.getId());
            userMessage.setOrderId(order.getId());
            userMessage.setContent("尊敬的" + username + ",您好。"
                    + "您单号为" + order.getId() + "的订单已"
                    + "于" + (1900 + timestamp.getYear()) + "年" + (timestamp.getMonth() + 1) + "月" + timestamp.getDate() + "日"
                    + timestamp.getHours() + "时" + timestamp.getMinutes() + "分" + timestamp.getSeconds() + "秒"
                    + "被维修人员确认，您可以进入xx页面查看详情。");
            for (String s : register.getSessionIds(user.getUsername())) {
                template.convertAndSendToUser(s, userSocketPath, JSON.toJSONString(userMessage), createHeaders(s));
            }
            messageRepository.save(userMessage);
        }
    }

    @Override
    public void pushFixingAndSave(List<Long> orderIds) {
        for (Long id : orderIds) {
            InstOrder order = instOrderRepository.getOne(id);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            order.setFixTime(timestamp);
            if (order.getEnumMaintainStatus() == MaintainStatus.CONFIRMED) {
                order.setMaintainStatus(MaintainStatus.FIXING);
            } else {
                throw OperationError;
            }
            instOrderRepository.save(order);
            User user = userRepository.getOne(order.getOwnerId());
            Instrument instrument = order.getInstrument();

            //创建消息并发送给仪器所有者
            Message userMessage = new Message();
            String username = user.getName() == null ? user.getUsername() : user.getName();
            userMessage.setMachineId(instrument.getId());
            userMessage.setThresholdValue(instrument.getThresholdValue());
            userMessage.setCreateTime(timestamp);
            userMessage.setUid(user.getId());
            userMessage.setInstName(instrument.getName());
            userMessage.setStatus(order.getEnumMaintainStatus());
            userMessage.setOrderId(order.getId());
            userMessage.setContent("尊敬的" + username + ",您好。"
                    + "您单号为" + order.getId() + "的订单已"
                    + "于" + (1900 + timestamp.getYear()) + "年" + (timestamp.getMonth() + 1) + "月" + timestamp.getDate() + "日"
                    + timestamp.getHours() + "时" + timestamp.getMinutes() + "分" + timestamp.getSeconds() + "秒"
                    + "被维修人员开始维修，您可以进入xx页面查看详情。");
            for (String s : register.getSessionIds(user.getUsername())) {
                template.convertAndSendToUser(s, userSocketPath, JSON.toJSONString(userMessage), createHeaders(s));
            }
            messageRepository.save(userMessage);
        }
    }

    @Override
    public void pushFinishAndSave(List<Long> orderIds) {
        for (Long id : orderIds) {
            InstOrder order = instOrderRepository.getOne(id);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            order.setFinishTime(timestamp);
            if (order.getEnumMaintainStatus() == MaintainStatus.FIXING) {
                order.setMaintainStatus(MaintainStatus.FINISHED);
            } else {
                throw OperationError;
            }
            instOrderRepository.save(order);
            User user = userRepository.getOne(order.getOwnerId());
            Instrument instrument = order.getInstrument();

            //创建消息并发送给仪器所有者
            Message userMessage = new Message();
            String username = user.getName() == null ? user.getUsername() : user.getName();
            userMessage.setMachineId(instrument.getId());
            userMessage.setThresholdValue(instrument.getThresholdValue());
            userMessage.setCreateTime(timestamp);
            userMessage.setInstName(instrument.getName());
            userMessage.setStatus(order.getEnumMaintainStatus());
            userMessage.setUid(user.getId());
            userMessage.setOrderId(order.getId());
            userMessage.setContent("尊敬的" + username + ",您好。"
                    + "您单号为" + order.getId() + "的订单已"
                    + "于" + (1900 + timestamp.getYear()) + "年" + (timestamp.getMonth() + 1) + "月" + timestamp.getDate() + "日"
                    + timestamp.getHours() + "时" + timestamp.getMinutes() + "分" + timestamp.getSeconds() + "秒"
                    + "被维修人员确认完结，您可以进入xx页面查看详情。");
            for (String s : register.getSessionIds(user.getUsername())) {
                template.convertAndSendToUser(s, userSocketPath, JSON.toJSONString(userMessage), createHeaders(s));
            }
            messageRepository.save(userMessage);
        }
    }

    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create();
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }
}
