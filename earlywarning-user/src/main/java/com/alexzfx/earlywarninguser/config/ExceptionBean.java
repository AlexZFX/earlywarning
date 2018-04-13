package com.alexzfx.earlywarninguser.config;

import com.alexzfx.earlywarninguser.exception.BaseException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Author : Alex
 * Date : 2018/3/20 16:56
 * Description :
 */
@Configuration
public class ExceptionBean {

    @Bean
    public BaseException UnknownAccountError() {
        return new BaseException(1000, "账户不存在");
    }

    @Bean
    public BaseException WrongPasswordError() {
        return new BaseException(1001, "密码错误");
    }

    @Bean
    public BaseException AccountExistError() {
        return new BaseException(1002, "账户已存在");
    }

    @Bean
    public BaseException VerCodeError() {
        return new BaseException(1003, "验证码错误");
    }

    @Bean
    public BaseException PasswordValidError() {
        return new BaseException(1004, "密码必须包含数字、字母、特殊字符三种,\n长度属于6-16位之间");
    }

    @Bean
    public BaseException MailSendError() {
        return new BaseException(2000, "邮件发送失败");
    }

    @Bean
    public BaseException AuthFailError() {
        return new BaseException(2001, "验证失败");
    }

    @Bean
    public BaseException FileTransError() {
        return new BaseException(3000, "文件转存失败");
    }
}
