package com.alexzfx.earlywarninguser.controller;

import com.alexzfx.earlywarninguser.entity.User;
import com.alexzfx.earlywarninguser.entity.request.Password;
import com.alexzfx.earlywarninguser.entity.request.RequestList;
import com.alexzfx.earlywarninguser.exception.BaseException;
import com.alexzfx.earlywarninguser.service.MailService;
import com.alexzfx.earlywarninguser.service.UserService;
import com.alexzfx.earlywarninguser.util.BaseResponse;
import com.alexzfx.earlywarninguser.util.VerCodeUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.alexzfx.earlywarninguser.util.BaseResponse.EMPTY_SUCCESS_RESPONSE;

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

    private final BaseException WrongPasswordError;

    private final BaseException UnknownAccountError;


    @Autowired(required = false)
    public UserController(UserService userService, MailService mailService, HttpServletResponse response, BaseException VerCodeError, BaseException WrongPasswordError, BaseException UnknownAccountError) {
        this.userService = userService;
        this.mailService = mailService;
        this.response = response;
        this.VerCodeError = VerCodeError;
        this.WrongPasswordError = WrongPasswordError;
        this.UnknownAccountError = UnknownAccountError;
    }

    @GetMapping("/admin/getUserInfo")
    @RequiresRoles("admin")
    public BaseResponse<Page> getAllUserInfo(@RequestParam("roleName") String roleName, @RequestParam(value = "keyWord", required = false) String keyWord, @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page page = userService.getAllUserInfo(roleName, keyWord, pageable);
        return new BaseResponse<>(page);
    }

    @PostMapping("/admin/createMaintainer")
    @RequiresRoles(value = {"admin"})
    public BaseResponse createMaintainer(@RequestBody User user) {
        userService.createMaintainer(user);
        return EMPTY_SUCCESS_RESPONSE;
    }

    @PostMapping("/admin/lockUser")
    @RequiresRoles(value = {"admin"})
    public BaseResponse lockUsers(@RequestBody RequestList list) {
        userService.lockUsers(list.getIntIds());
        return EMPTY_SUCCESS_RESPONSE;
    }

    @PostMapping("/admin/unlockUser")
    @RequiresRoles(value = {"admin"})
    public BaseResponse unlockUsers(@RequestBody RequestList list) {
        userService.unlockUsers(list.getIntIds());
        return EMPTY_SUCCESS_RESPONSE;
    }

    @GetMapping("/getVerCode")
    public void getVerCode() {
        Subject subject = SecurityUtils.getSubject();
        try {
            response.setContentType("image/png");
            VerCodeUtil.getVerCode(subject.getSession(), response.getOutputStream());
        } catch (IOException e) {
            throw new BaseException();
        }
    }


    @PostMapping("/login")
    public BaseResponse<User> login(@RequestBody User user) {
        Subject subject = SecurityUtils.getSubject();
        if (!VerCodeUtil.checkVerCode(subject.getSession(), user.getVerCode())) {
            throw VerCodeError;
        }
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        try {
            subject.login(token);
        } catch (IncorrectCredentialsException e) {
            throw WrongPasswordError;
        } catch (UnknownAccountException e) {
            throw UnknownAccountError;
        } catch (BaseException e) {
            if (e.getRet() == 1006) {
                throw e;
            } else {
                throw new BaseException();
            }
        }
        user = (User) SecurityUtils.getSubject().getPrincipal();
        return new BaseResponse<>(user);
    }

    @GetMapping("/getUserInfo")
    public BaseResponse<User> getUserInfo() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (user != null) {
            user = userService.getUserByUsername(user.getUsername());
        }
        return new BaseResponse<>(user);
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

    //TODO 完善逻辑
    @PostMapping("/resetPassword")
    public BaseResponse resetPassword(@RequestBody Password password) {
        String newPassword = password.getNewPassword();
        userService.resetPassword(newPassword);
        return EMPTY_SUCCESS_RESPONSE;
    }

    @PostMapping("/updatePassword")
    public BaseResponse updatePassword(@RequestBody Password password) {
        userService.updatePassword(password);
        return EMPTY_SUCCESS_RESPONSE;
    }

    @GetMapping("/logout")
    public BaseResponse logout() {
        SecurityUtils.getSubject().logout();
        return EMPTY_SUCCESS_RESPONSE;
    }

}
