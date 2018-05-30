package com.alexzfx.earlywarninguser.service.impl;

import com.alexzfx.earlywarninguser.entity.InstOrder;
import com.alexzfx.earlywarninguser.entity.Instrument;
import com.alexzfx.earlywarninguser.entity.MachineData;
import com.alexzfx.earlywarninguser.entity.User;
import com.alexzfx.earlywarninguser.entity.e.MaintainStatus;
import com.alexzfx.earlywarninguser.exception.BaseException;
import com.alexzfx.earlywarninguser.repository.*;
import com.alexzfx.earlywarninguser.service.MachineDataService;
import com.alexzfx.earlywarninguser.service.MailService;
import com.alexzfx.earlywarninguser.service.MsgPushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Author : Alex
 * Date : 2018/4/15 19:50
 * Description :
 */
@Service
@Slf4j
@Transactional
public class MachineDataServiceImpl implements MachineDataService {


    private final InstrumentRepository instrumentRepository;

    private final MachineDataRepository machineDataRepository;

    private final MailService mailService;

    private final MsgPushService msgPushService;

    private final InstOrderRepository instOrderRepository;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final BaseException NoMaintainerError;

    @Autowired
    public MachineDataServiceImpl(InstrumentRepository instrumentRepository, MachineDataRepository machineDataRepository, MailService mailService, MsgPushService msgPushService, InstOrderRepository instOrderRepository, UserRepository userRepository, RoleRepository roleRepository, BaseException NoMaintainerError) {
        this.instrumentRepository = instrumentRepository;
        this.machineDataRepository = machineDataRepository;
        this.mailService = mailService;
        this.msgPushService = msgPushService;
        this.instOrderRepository = instOrderRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.NoMaintainerError = NoMaintainerError;
    }

    /**
     * 处理数据，保存。消息发送，存储
     *
     * @param machineData
     */
    @Override
    public void handleData(MachineData machineData) {
        try {
            machineDataRepository.save(machineData);
            Instrument instrument = instrumentRepository.findById(machineData.getMachineId()).get();
            if (instrument.getThresholdValue() < machineData.getData()) {
                //TODO 先判断是否正在被维修，验证JPA查询返回值
                //如果出问题的这台仪器不是正在被修理时，进行接下来的预警以及推送
                Long orderId = instOrderRepository.findIsFixing(instrument.getId(), MaintainStatus.FINISHED);
                if (orderId == null) {
                    //对维修人员负载均衡
                    Integer maintainid = roleRepository.findByName("maintainer").getId();
                    //查询当前最为空闲的5个维修人员
                    List<Integer> maintainerIds = userRepository.findFreeMaintainer(maintainid, MaintainStatus.FINISHED);
                    if (maintainerIds.isEmpty()) {
                        throw NoMaintainerError;
                    }
                    Collections.shuffle(maintainerIds);
                    //不直接拿到完整对象的话，会导致后面方法因为懒加载session的丢失而出现异常
                    User maintainer = userRepository.findById(maintainerIds.get(0)).get();

                    //创建并设置订单
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    InstOrder order = new InstOrder();
                    //订单号规则，时间戳前十位+机器id的后三位
                    String time = String.valueOf(timestamp.getTime()).substring(0, 10);
                    String num = String.format("%03d", instrument.getId() % 1000);
                    order.setId(Long.parseLong(time + num));
                    order.setInstrument(instrument);
                    order.setMaintainStatus(MaintainStatus.WAITCONFIRM);
                    order.setCreateTime(timestamp);
                    order.setOwnerId(instrument.getCreater().getId());
                    order.setMaintainerId(maintainer.getId());
                    instOrderRepository.save(order);

                    //TODO 数据推送
                    msgPushService.pushAndSave(instrument, machineData, maintainer, order);
                    //TODO 邮件发送
                    mailService.sendWarningEmail(instrument, machineData, maintainer, order);
                }
            }
        } catch (EntityNotFoundException | NoSuchElementException e) {
            log.error("消息中的仪器不存在");
        }
    }

    @Override
    public List<MachineData> getAllData() {
        return machineDataRepository.findAll();
    }

    @Override
    public List<MachineData> getDataByMachineId(Integer machineId) {
        return machineDataRepository.findByMachineId(machineId);
    }
}
