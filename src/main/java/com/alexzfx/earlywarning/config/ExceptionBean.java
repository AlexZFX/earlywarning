package com.alexzfx.earlywarning.config;

import com.alexzfx.earlywarning.exception.BaseException;
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
    public BaseException MailSendError() {
        return new BaseException(2000, "邮件发送失败");
    }

    @Bean
    public BaseException AuthFailError() {
        return new BaseException(2001, "验证失败");
    }
}
