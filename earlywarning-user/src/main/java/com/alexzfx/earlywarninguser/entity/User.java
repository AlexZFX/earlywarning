package com.alexzfx.earlywarninguser.entity;

import com.alexzfx.earlywarninguser.entity.e.LockStatus;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private Integer id;
    @Column(unique = true)
    private String username;
    private String name;
    @Column(unique = true)
    private String email;//需要验证
    @Column(columnDefinition = "varchar(15)")
    private String phone;
    @Column(columnDefinition = "varchar(2048)")
    private String description;
    //默认ORDINAL 按枚举序数存储，存的是int，STRING表示按枚举名存储，数据库存储的是String
    @Enumerated(EnumType.ORDINAL)
    private LockStatus isEmailLocked = LockStatus.LOCKED;
    @JSONField(serialize = false)
    private String password;
    private LockStatus isLocked = LockStatus.UNLOCKED;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "UserRole", joinColumns = {@JoinColumn(name = "uid")}, inverseJoinColumns = {@JoinColumn(name = "roleId")})
    private List<Role> roles;
    private String avatar;//头像路径
    @Transient
    @JSONField(serialize = false)
    private String verCode;

    public int getIsEmailLocked() {
        return isEmailLocked.getId();
    }

    public void setIsEmailLocked(LockStatus isEmailLocked) {
        this.isEmailLocked = isEmailLocked;
    }

    public int getIsLocked() {
        return isLocked.getId();
    }

    public void setIsLocked(LockStatus isLocked) {
        this.isLocked = isLocked;
    }

    @JSONField(serialize = false)
    public Set<String> getRoleNames() {
        Set set = new HashSet();
        if (roles != null) { //不判空的话，在返回时，序列化会导致空指针异常。不知道为什么序列化时 会调用这个方法
            for (Role role : roles) {
                set.add(role.getName());
            }
        }
        return set;
    }

}

