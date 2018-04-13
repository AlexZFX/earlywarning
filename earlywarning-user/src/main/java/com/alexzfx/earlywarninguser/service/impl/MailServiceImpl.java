package com.alexzfx.earlywarninguser.service.impl;

import com.alexzfx.earlywarninguser.entity.User;
import com.alexzfx.earlywarninguser.entity.e.LockStatus;
import com.alexzfx.earlywarninguser.exception.BaseException;
import com.alexzfx.earlywarninguser.repository.UserRepository;
import com.alexzfx.earlywarninguser.service.MailService;
import com.alexzfx.earlywarninguser.util.VerCodeUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

/**
 * Author : Alex
 * Date : 2018/3/22 9:53
 * Description :
 */
@Service
@Transactional
public class MailServiceImpl implements MailService {

    private final JavaMailSender sender;

    private final StringRedisTemplate redisTemplate;

    private final BaseException AuthFailError;

    private final UserRepository userRepository;

    private final Base64.Encoder encoder = Base64.getEncoder();

    private final Base64.Decoder decoder = Base64.getDecoder();

    @Value("${BaseUrl}")
    private String BaseUrl;

    @Value("${spring.mail.username}")
    private String from;

    @Resource
    private BaseException MailSendError;

    @Autowired
    public MailServiceImpl(JavaMailSender sender, StringRedisTemplate redisTemplate, BaseException AuthFailError, UserRepository userRepository) {
        this.sender = sender;
        this.redisTemplate = redisTemplate;
        this.AuthFailError = AuthFailError;
        this.userRepository = userRepository;
    }


    @Override
    public void sendAuthMail(String mail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(mail);
        message.setSubject("【EarlyWarning邮箱验证】");
        //得到的是realm里面doGetAuthenticationInfo时new SimpleAuthenticationInfo的第一个构造参数
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        String authCode = VerCodeUtil.getAuthCode();
        byte[] authMsg = authCode.getBytes();
        int uid = user.getId();
//        int uid = 1;
        redisTemplate.opsForValue().set("Auth:" + String.valueOf(uid), authCode);//将验证码存入redis
        redisTemplate.expire(String.valueOf(uid), 30, TimeUnit.MINUTES);//设置过期时间为30min
        String emailAuthMsg = "如果这是你在【EarlyWarning】注册的账号，请在30分钟内点击下方的链接以确认你的身份，如果不是，请忽略\n\n";
        String msg = emailAuthMsg + BaseUrl +
                "/authEmail?uid=" + uid + "&" +
                "authCode=" + encoder.encodeToString(authMsg);
        message.setText(msg);
        try {
            sender.send(message);
        } catch (Exception e) {
            throw MailSendError;
        }
        user.setEmail(mail);
        userRepository.save(user);
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
}
