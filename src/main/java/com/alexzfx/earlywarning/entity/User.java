package com.alexzfx.earlywarning.entity;

import com.alexzfx.earlywarning.entity.e.LockStatus;
import com.alexzfx.earlywarning.util.GsonUtil.GsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Author : Alex
 * Date : 2018/3/20 15:47
 * Description :
 */
@Entity
@Data
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String username;
    private String name;
    private String email;//需要验证
    private String description;
    private LockStatus isEmailLocked = LockStatus.LOCKED;
    @GsonIgnore
    private String password;
    private LockStatus isLocked = LockStatus.LOCKED;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "UserRole", joinColumns = {@JoinColumn(name = "uid")}, inverseJoinColumns = {@JoinColumn(name = "roleId")})
    private List<Role> roles;
    private String avatar;//头像路径
}

