package com.alexzfx.earlywarninguser.service.impl;

import com.alexzfx.earlywarninguser.entity.Role;
import com.alexzfx.earlywarninguser.entity.User;
import com.alexzfx.earlywarninguser.entity.request.Password;
import com.alexzfx.earlywarninguser.exception.BaseException;
import com.alexzfx.earlywarninguser.repository.RoleRepository;
import com.alexzfx.earlywarninguser.repository.UserRepository;
import com.alexzfx.earlywarninguser.service.UserService;
import com.alexzfx.earlywarninguser.util.PasswordHash;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Collections;
import java.util.List;

/**
 * Author : Alex
 * Date : 2018/3/22 8:44
 * Description :
 */
@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final BaseException AccountExistError;

    private final String RootPath;

    private final BaseException FileTransError;

    private final BaseException PwdValidError;

    private final BaseException EmailValidError;

    private final BaseException WrongPasswordError;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BaseException AccountExistError, String RootPath, BaseException FileTransError, BaseException PwdValidError, BaseException EmailValidError, BaseException WrongPasswordError) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.AccountExistError = AccountExistError;
        this.RootPath = RootPath;
        this.FileTransError = FileTransError;
        this.PwdValidError = PwdValidError;
        this.EmailValidError = EmailValidError;
        this.WrongPasswordError = WrongPasswordError;
    }


    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void modifyUserInfo(User userInfo) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        user = userRepository.findByUsername(user.getUsername());
        user.setName(userInfo.getName());
        user.setDescription(userInfo.getDescription());
        userRepository.save(user);
    }

    @Override
    public void register(User user) {
        //TODO 打开
        if (!validPassword(user.getPassword())) {
            throw PwdValidError;
        }
//        if (!validEamil(user.getEmail())) {
//            throw EmailValidError;
//        }
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw AccountExistError;
        }
        try {
            user.setPassword(PasswordHash.createHash(user.getPassword()));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new BaseException(500, "密码加密失败");
        }
        //找到角色为用户
        Role role = roleRepository.findByName("user");
        user.setRoles(Collections.singletonList(role));
        userRepository.save(user);
    }

    @Override
    public String uploadAvatar(MultipartFile file) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        user = userRepository.findByUsername(user.getUsername());
        String url = "avatar/" + user.getUsername() + ".png";
        File avatar = new File(RootPath + url);
        if (!avatar.getParentFile().isDirectory()) {
            avatar.getParentFile().mkdirs();
        }
        try {
            file.transferTo(avatar.getAbsoluteFile());
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
            throw FileTransError;
        }
        url = "/" + url;
        user.setAvatar(url);
        userRepository.save(user);
        return url;
    }

    @Override
    public void updatePassword(Password password) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        user = userRepository.findByUsername(user.getUsername());
        String oldPassword = password.getOldPassword();
        try {
            PasswordHash.validatePassword(oldPassword, user.getPassword());
            if (!validPassword(password.getNewPassword())) {
                throw PwdValidError;
            }
            user.setPassword(PasswordHash.createHash(password.getNewPassword()));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new BaseException(500, "密码加密失败");
        }
        userRepository.save(user);
    }

    @Override
    public void resetPassword(String password) {
        if (!validPassword(password)) {
            throw PwdValidError;
        }
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        user = userRepository.findByUsername(user.getUsername());
        try {
            user.setPassword(PasswordHash.createHash(password));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new BaseException(500, "密码加密失败");
        }
        userRepository.save(user);
    }

    @Override
    public void createMaintainer(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw AccountExistError;
        }
        try {
            user.setPassword(PasswordHash.createHash(user.getPassword()));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new BaseException(500, "密码加密失败");
        }
        Role role = roleRepository.findByName("maintainer");
        user.setRoles(Collections.singletonList(role));
        userRepository.save(user);
    }

    @Override
    public Page getAllUserInfo(String roleName, String keyWord, Pageable pageable) {
        Role role = roleRepository.findByName(roleName);
        if (role == null) {
            return null;
        }
        if (keyWord != null) {
            return userRepository.findByRoleIdAndUsernameLike(role.getId(), "%" + keyWord + "%", pageable);
        } else {
            return userRepository.findByRoleId(role.getId(), pageable);
        }
    }

    @Override
    public void lockUsers(List<Integer> intIds) {
        Role role = roleRepository.findByName("admin");
        userRepository.lockUsers(intIds, role.getId());
    }

    @Override
    public void unlockUsers(List<Integer> intIds) {
        userRepository.unlockUsers(intIds);
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

    /**
     * 之前必须有内容且只能是字母（大小写）、数字、下划线(_)、减号（-）、点（.）
     * 和最后一个点（.）之间必须有内容且只能是字母（大小写）、数字、点（.）、减号（-），且两个点不能挨着
     * 最后一个点（.）之后必须有内容且内容只能是字母（大小写）、数字且长度为大于等于2个字节，小于等于6个字节
     *
     * @param email
     * @return 邮箱是否符合验证
     */
    private boolean validEamil(String email) {
        if (email == null) {
            return false;
        }
        return email.matches("^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$");
    }
}
