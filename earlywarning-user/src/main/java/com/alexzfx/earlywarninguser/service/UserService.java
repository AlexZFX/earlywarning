package com.alexzfx.earlywarninguser.service;

import com.alexzfx.earlywarninguser.entity.User;
import org.springframework.web.multipart.MultipartFile;

/**
 * Author : Alex
 * Date : 2018/3/21 17:15
 * Description :
 */
public interface UserService {

    User getUserById(int id);

    User getUserByUsername(String username);

    void modifyUserInfo(User user);

    void register(User user);

    String uploadAvatar(MultipartFile file);

    void updatePassword(String password);
}
