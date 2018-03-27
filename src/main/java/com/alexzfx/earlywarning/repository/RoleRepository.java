package com.alexzfx.earlywarning.repository;

import com.alexzfx.earlywarning.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Author : Alex
 * Date : 2018/3/26 19:49
 * Description :
 */
@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {
}
