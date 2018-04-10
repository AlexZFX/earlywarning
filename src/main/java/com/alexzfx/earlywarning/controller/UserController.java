package com.alexzfx.earlywarning.controller;

import com.alexzfx.earlywarning.entity.User;
import com.alexzfx.earlywarning.exception.BaseException;
import com.alexzfx.earlywarning.service.MailService;
import com.alexzfx.earlywarning.service.UserService;
import com.alexzfx.earlywarning.util.BaseResponse;
import com.alexzfx.earlywarning.util.VerCodeUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.alexzfx.earlywarning.util.BaseResponse.EMPTY_SUCCESS_RESPONSE;

/**
 * Author : Alex
 * Date : 2018/3/20 18:56
 * Description :
 */
@RestController
public class UserController {

    private final UserService userService;

    private final MailService mailService;

    private final HttpServletResponse response;

    private final BaseException VerCodeError;

    @Autowired(required = false)
    public UserController(UserService userService, MailService mailService, HttpServletResponse response, BaseException VerCodeError) {
        this.userService = userService;
        this.mailService = mailService;
        this.response = response;
        this.VerCodeError = VerCodeError;
    }

    @GetMapping("/getVerCode")
    public void getVerCode() {
        Subject subject = SecurityUtils.getSubject();
        try {
            response.setContentType("image/png");
            VerCodeUtil.getVetCode(subject.getSession(), response.getOutputStream());
        } catch (IOException e) {
            throw new BaseException();
        }
    }


    @PostMapping("/login")
    public BaseResponse login(@RequestBody User user) {
        Subject subject = SecurityUtils.getSubject();
        if (!VerCodeUtil.checkVerCode(subject.getSession(), user.getVerCode())) {
            throw VerCodeError;
        }
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        subject.login(token);
        return EMPTY_SUCCESS_RESPONSE;
    }

    @PostMapping("/register")
    public BaseResponse register(@RequestBody User user) {
        userService.register(user);
        return EMPTY_SUCCESS_RESPONSE;
    }

    @GetMapping("/authEmail")
    public BaseResponse authEmail(@RequestParam("uid") Integer uid, @RequestParam("authCode") String authCode) {
        mailService.validAuthMail(uid, authCode);
        return EMPTY_SUCCESS_RESPONSE;
    }

    @PostMapping("/modifyUserInfo")
    public BaseResponse modifyUserInfo(@RequestBody User userInfo) {
        userService.modifyUserInfo(userInfo);
        return EMPTY_SUCCESS_RESPONSE;
    }

    @PostMapping("/uploadAvatar")
    public BaseResponse<String> uploadAvatar(@RequestParam("avatar") MultipartFile file) {
        String url = userService.uploadAvatar(file);
        return new BaseResponse<>(url);
    }

    @PostMapping("/updateEmail")
    public BaseResponse uploadEmail(@RequestBody User user) {
        mailService.sendAuthMail(user.getEmail());
        return EMPTY_SUCCESS_RESPONSE;
    }

    @PostMapping("/updatePassword")
    public BaseResponse updatePassword(@RequestBody User user) {
        String password = user.getPassword();
        userService.updatePassword(password);
        return EMPTY_SUCCESS_RESPONSE;
    }


}
