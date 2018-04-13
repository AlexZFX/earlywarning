package com.alexzfx.earlywarninguser.repository;

import com.alexzfx.earlywarninguser.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Author : Alex
 * Date : 2018/3/21 17:15
 * Description :
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
