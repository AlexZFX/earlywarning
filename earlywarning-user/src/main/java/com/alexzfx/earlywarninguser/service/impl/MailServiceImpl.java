package com.alexzfx.earlywarninguser.service.impl;

import com.alexzfx.earlywarninguser.entity.InstOrder;
import com.alexzfx.earlywarninguser.entity.Instrument;
import com.alexzfx.earlywarninguser.entity.MachineData;
import com.alexzfx.earlywarninguser.entity.User;
import com.alexzfx.earlywarninguser.entity.e.LockStatus;
import com.alexzfx.earlywarninguser.exception.BaseException;
import com.alexzfx.earlywarninguser.repository.UserRepository;
import com.alexzfx.earlywarninguser.service.MailService;
import com.alexzfx.earlywarninguser.util.VerCodeUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

/**
 * Author : Alex
 * Date : 2018/3/22 9:53
 * Description :
 */
@Service
//@Transactional
public class MailServiceImpl implements MailService {

    private final JavaMailSender sender;

    private final StringRedisTemplate redisTemplate;

    private final BaseException AuthFailError;

    private static final String AUTHMSG = "如果这是你在【EarlyWarning】注册的账号，请在30分钟内点击下方的链接以确认你的身份，如果不是，请忽略\n\n";

    private final UserRepository userRepository;


    private final Base64.Encoder encoder = Base64.getEncoder();

    private final Base64.Decoder decoder = Base64.getDecoder();
    private final BaseException EmailExistError;

    @Value("${BaseUrl}")
    private String BaseUrl;

    @Value("${spring.mail.username}")
    private String from;
    private final BaseException MailSendError;

    private final BaseException EmailValidError;

    @Autowired
    public MailServiceImpl(JavaMailSender sender, StringRedisTemplate redisTemplate, BaseException AuthFailError, BaseException EmailExistError, UserRepository userRepository, BaseException MailSendError, BaseException EmailValidError) {
        this.sender = sender;
        this.redisTemplate = redisTemplate;
        this.AuthFailError = AuthFailError;
        this.EmailExistError = EmailExistError;
        this.userRepository = userRepository;
        this.MailSendError = MailSendError;
        this.EmailValidError = EmailValidError;
    }


    @Override
    public void sendAuthMail(String mail) {
        if (!validEamil(mail)) {
            throw EmailValidError;
        }
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        user = userRepository.findByUsername(user.getUsername());
        int uid = user.getId();
        user.setEmail(mail);
        user.setIsEmailLocked(LockStatus.LOCKED);
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw EmailExistError;
        }
        //发现这段操作很耗时间，另开线程单独执行
        Thread thread = new Thread(() -> {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(mail);
            message.setSubject("【EarlyWarning邮箱验证】");
            //得到的是realm里面doGetAuthenticationInfo时new SimpleAuthenticationInfo的第一个构造参数
            String authCode = VerCodeUtil.getAuthCode();
            byte[] authMsg = authCode.getBytes();
//        int uid = 1;
            redisTemplate.opsForValue().set("Auth:" + String.valueOf(uid), authCode);//将验证码存入redis
            redisTemplate.expire(String.valueOf("Auth:" + uid), 30, TimeUnit.MINUTES);//设置过期时间为30min
            String msg = AUTHMSG + BaseUrl +
                    "/authEmail?uid=" + uid + "&" +
                    "authCode=" + encoder.encodeToString(authMsg);
            message.setText(msg);
            try {
                sender.send(message);
            } catch (MailException e) {
                throw MailSendError;
            }
        });
        thread.start();
    }

    /**
     * 对用户邮箱验证后修改用户邮箱锁定信息
     *
     * @param uid      用户id
     * @param authCode 链接中所带有的Base64的验证码
     */
    @Override
    public void validAuthMail(int uid, String authCode) {
        String realCode = redisTemplate.opsForValue().get("Auth:" + uid);
        if (!Arrays.equals(decoder.decode(authCode), realCode.getBytes())) {
            throw AuthFailError;
        } else {
            User user = userRepository.getOne(uid);
            user.setIsEmailLocked(LockStatus.UNLOCKED);
            userRepository.save(user);
        }
    }

    @Override
    public void sendWarningEmail(Instrument instrument, MachineData machineData, User maintainer, InstOrder order) {
        User user = instrument.getCreater();
        Timestamp timestamp = order.getCreateTime();
        String username = user.getName() == null ? user.getUsername() : user.getName();
        String maintainerName = maintainer.getName() == null ? maintainer.getUsername() : maintainer.getName();
        new Thread(() -> {
            if (user.getIsEmailLocked() != LockStatus.LOCKED.getId()) {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(from);
                message.setTo(user.getEmail());
                message.setSubject("【EarlyWarning通知】");
                String msg = "尊敬的" + username + ",您好。"
                        + "您所创建的仪器" + instrument.getName() + "(" + instrument.getId() + ")"
                        + "于" + (1900 + timestamp.getYear()) + "年" + timestamp.getMonth() + "月" + timestamp.getDay() + "日"
                        + timestamp.getHours() + "时" + timestamp.getMinutes() + "分" + timestamp.getSeconds() + "秒"
                        + "发生了告警，仪器数值为" + machineData.getData() + "(" + instrument.getThresholdValue() + ")。"
                        + "系统已将此任务分配给了工作人员" + maintainerName
                        + ",其联系方式为" + maintainer.getEmail() + ",你可以登录后进入xx页面查看详情。";
                message.setText(msg);
                try {
                    sender.send(message);
                } catch (MailException e) {
                    throw MailSendError;
                }
            }
        }).start();
        new Thread(() -> {
            if (maintainer.getIsEmailLocked() != LockStatus.LOCKED.getId()) {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(from);
                message.setTo(maintainer.getEmail());
                message.setSubject("【EarlyWarning通知】");
                String msg = ("尊敬的" + maintainerName + ",您好。"
                        + "用户" + username + "所创建的仪器" + instrument.getName() + "(" + instrument.getId() + ")"
                        + "于" + (1900 + timestamp.getYear()) + "年" + timestamp.getMonth() + "月" + timestamp.getDay() + "日"
                        + timestamp.getHours() + "时" + timestamp.getMinutes() + "分" + timestamp.getSeconds() + "秒"
                        + "发生了告警，仪器数值为" + machineData.getData() + "(" + instrument.getThresholdValue() + ")"
                        + "系统已将此任务分配给了您" + "用户联系方式为" + user.getEmail()
                        + ",你可以登录后进入xx页面查看详情。请及时处理");
                message.setText(msg);
                try {
                    sender.send(message);
                } catch (MailException e) {
                    throw MailSendError;
                }
            }
        }).start();
    }


    /**
     * 之前必须有内容且只能是字母（大小写）、数字、下划线(_)、减号（-）、点（.）
     * 和最后一个点（.）之间必须有内容且只能是字母（大小写）、数字、点（.）、减号（-），且两个点不能挨着
     * 最后一个点（.）之后必须有内容且内容只能是字母（大小写）、数字且长度为大于等于2个字节，小于等于6个字节
     *
     * @param email
     * @return 邮箱是否符合验证
     */
    private boolean validEamil(String email) {
        return email.matches("^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$");
    }
}
