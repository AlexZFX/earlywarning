package com.alexzfx.earlywarninguser.entity;

import com.alexzfx.earlywarninguser.entity.e.LockStatus;
import com.alibaba.fastjson.annotation.JSONField;
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
    private Integer id;
    private String name;
    private String description;
    private LockStatus isLocked = LockStatus.UNLOCKED;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "RolePermission", joinColumns = {@JoinColumn(name = "pid")}, inverseJoinColumns = {@JoinColumn(name = "roleId")})
    @JSONField(serialize = false)
    private List<Role> roles;

    @Override
    public String toString() {
        return "Permission{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isLocked=" + isLocked +
                '}';
    }

    public int getIsLocked() {
        return isLocked.getId();
    }

    public void setIsLocked(LockStatus isLocked) {
        this.isLocked = isLocked;
    }
}
