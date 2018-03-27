package com.alexzfx.earlywarning.entity;

import com.alexzfx.earlywarning.entity.e.LockStatus;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * Author : Alex
 * Date : 2018/3/21 11:45
 * Description :
 */
@Data
@Entity
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    private LockStatus isLocked = LockStatus.UNLOCKED;
    @ManyToMany
    @JoinTable(name = "RolePermission", joinColumns = {@JoinColumn(name = "pid")}, inverseJoinColumns = {@JoinColumn(name = "roleId")})
    private List<Role> roles;
}
