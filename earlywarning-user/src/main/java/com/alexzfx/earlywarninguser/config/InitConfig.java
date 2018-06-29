package com.alexzfx.earlywarninguser.config;

import com.alexzfx.earlywarninguser.entity.Role;
import com.alexzfx.earlywarninguser.entity.User;
import com.alexzfx.earlywarninguser.repository.RoleRepository;
import com.alexzfx.earlywarninguser.repository.UserRepository;
import com.alexzfx.earlywarninguser.util.PasswordHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Collections;

/**
 * Author : Alex
 * Date : 2018/6/20 19:12
 * Description :
 */
@Component
public class InitConfig {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Autowired
    public InitConfig(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    private void init() throws InvalidKeySpecException, NoSuchAlgorithmException {

        if (roleRepository.findAll().size() == 0) {
            roleRepository.save(new Role("admin", "管理员"));
            roleRepository.save(new Role("maintainer", "维修人员"));
            roleRepository.save(new Role("user", "用户"));
            User user = userRepository.findByUsername("admin");
            if (user == null) {
                user = new User();
                user.setUsername("admin");
                user.setPassword(PasswordHash.createHash("0925"));
                Role role = roleRepository.findByName("admin");
                user.setRoles(Collections.singletonList(role));
                userRepository.save(user);
            }
        }

    }

}
