package com.alexzfx.earlywarninguser.service;

import com.alexzfx.earlywarninguser.entity.User;
import com.alexzfx.earlywarninguser.entity.request.Password;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Author : Alex
 * Date : 2018/3/21 17:15
 * Description :
 */
public interface UserService {

    User getUserByUsername(String username);

    void modifyUserInfo(User user);

    void register(User user);

    String uploadAvatar(MultipartFile file);

    void updatePassword(Password password);

    void resetPassword(String newPassword);

    void createMaintainer(User user);

    Page getAllUserInfo(String roleName, String keyWord, Pageable pageable);

    void lockUsers(List<Integer> intIds);

    void unlockUsers(List<Integer> intIds);
}
