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
    public BaseException PermissionDenied() {
        return new BaseException(403, "权限不足");
    }

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
    public BaseException PwdValidError() {
        return new BaseException(1004, "密码必须包含数字、字母、特殊字符三种,\n长度属于6-16位之间");
    }

    @Bean
    public BaseException EmailExistError() {
        return new BaseException(1005, "邮箱已存在");
    }

    @Bean
    public BaseException MailSendError() {
        return new BaseException(2000, "邮件发送失败");
    }

    @Bean
    public BaseException AuthFailError() {
        return new BaseException(2001, "邮箱验证失败");
    }

    @Bean
    public BaseException EmailValidError() {
        return new BaseException(2002, "邮箱不符合规定");
    }

    @Bean
    public BaseException FileTransError() {
        return new BaseException(3000, "文件转存失败");
    }

    @Bean
    public BaseException NoMaintainerError() {
        return new BaseException(4000, "没有搜到维修人员啊兄弟");
    }

    @Bean
    public BaseException NotFoundError() {
        return new BaseException(4001, "查找不到对应");
    }

    @Bean
    public BaseException DeleteFailError() {
        return new BaseException(5000, "删除失败，请检查删除是否安全");
    }

}
