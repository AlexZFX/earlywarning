package com.alexzfx.earlywarning.service;

import com.alexzfx.earlywarning.entity.User;
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
