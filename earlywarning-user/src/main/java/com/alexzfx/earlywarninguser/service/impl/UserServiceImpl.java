package com.alexzfx.earlywarninguser.service.impl;

import com.alexzfx.earlywarninguser.entity.Role;
import com.alexzfx.earlywarninguser.entity.User;
import com.alexzfx.earlywarninguser.exception.BaseException;
import com.alexzfx.earlywarninguser.repository.RoleRepository;
import com.alexzfx.earlywarninguser.repository.UserRepository;
import com.alexzfx.earlywarninguser.service.UserService;
import com.alexzfx.earlywarninguser.util.PasswordHash;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Collections;

/**
 * Author : Alex
 * Date : 2018/3/22 8:44
 * Description :
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final BaseException AccountExistError;

    private final String RootPath;

    private final BaseException FileTransError;

    private final BaseException PasswordValidError;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BaseException AccountExistError, String RootPath, BaseException FileTransError, BaseException PasswordValidError) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.AccountExistError = AccountExistError;
        this.RootPath = RootPath;
        this.FileTransError = FileTransError;
        this.PasswordValidError = PasswordValidError;
    }


    @Override
    public User getUserById(int id) {
        return userRepository.getOne(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void modifyUserInfo(User userInfo) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        user.setName(userInfo.getName());
        user.setDescription(user.getDescription());
        userRepository.save(user);
    }

    @Override
    public void register(User user) {
        if (!validPassword(user.getPassword())) {
            throw PasswordValidError;
        }
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw AccountExistError;
        }
        try {
            user.setPassword(PasswordHash.createHash(user.getPassword()));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new BaseException(500, "密码加密失败");
        }
        //找到角色为用户
        Role role = roleRepository.getOne(1);
        user.setRoles(Collections.singletonList(role));
        userRepository.save(user);
    }

    @Override
    public String uploadAvatar(MultipartFile file) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        String url = "/avatar/" + user.getUsername() + ".png";
        File avatar = new File(RootPath + url);
        if (!avatar.getParentFile().isDirectory()) {
            avatar.getParentFile().mkdirs();
        }
        try {
            file.transferTo(avatar);
        } catch (IOException e) {
            throw FileTransError;
        }
        user.setAvatar(url);
        userRepository.save(user);
        return url;
    }

    @Override
    public void updatePassword(String password) {
        if (!validPassword(password)) {
            throw PasswordValidError;
        }
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        try {
            user.setPassword(PasswordHash.createHash(password));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new BaseException(500, "密码加密失败");
        }
        userRepository.save(user);
    }

    /**
     * 验证密码必须满足 包含数字、字母、特殊字符三种，且长度在 6-16位之间
     *
     * @param password
     * @return true 为满足，false为不满足
     */
    private boolean validPassword(String password) {
        return password.matches("^.*[a-zA-Z]+.*$")
                && password.matches("^.*[0-9]+.*$")
                && password.matches("^.*[/^/$/.//,;:'!@#%&/*/|/?/+/(/)/[/]/{/}]+.*$")
                && password.length() > 5
                && password.length() < 17;
    }
}
