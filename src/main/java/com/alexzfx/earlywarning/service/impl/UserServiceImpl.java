package com.alexzfx.earlywarning.service.impl;

import com.alexzfx.earlywarning.entity.Role;
import com.alexzfx.earlywarning.entity.User;
import com.alexzfx.earlywarning.exception.BaseException;
import com.alexzfx.earlywarning.repository.RoleRepository;
import com.alexzfx.earlywarning.repository.UserRepository;
import com.alexzfx.earlywarning.service.UserService;
import com.alexzfx.earlywarning.util.PasswordHash;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
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

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BaseException AccountExistError) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.AccountExistError = AccountExistError;
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
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw AccountExistError;
        }
        try {
            user.setPassword(PasswordHash.createHash(user.getPassword()));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new BaseException(500, "密码加密失败");
        }
        //找到角色为用户
        Role role = roleRepository.getOne(0);
        user.setRoles(Collections.singletonList(role));
        userRepository.save(user);
    }
}
