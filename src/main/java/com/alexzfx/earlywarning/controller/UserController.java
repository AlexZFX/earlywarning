package com.alexzfx.earlywarning.controller;

import com.alexzfx.earlywarning.entity.User;
import com.alexzfx.earlywarning.service.MailService;
import com.alexzfx.earlywarning.service.UserService;
import com.alexzfx.earlywarning.util.BaseResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    public UserController(UserService userService, MailService mailService) {
        this.userService = userService;
        this.mailService = mailService;
    }


    @PostMapping("/login")
    public BaseResponse login(@RequestBody User user) {
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(),user.getPassword());
        SecurityUtils.getSubject().login(token);
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

}
