package com.alexzfx.earlywarninguser.entity;

import com.alexzfx.earlywarninguser.entity.e.LockStatus;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Author : Alex
 * Date : 2018/3/21 11:07
 * Description :
 */

@Data
@Entity
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String name;
    private String description;
    @Column(columnDefinition = "int not null default 1")
    private LockStatus isLocked = LockStatus.UNLOCKED;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "UserRole", joinColumns = {@JoinColumn(name = "roleId")}, inverseJoinColumns = {@JoinColumn(name = "uid")})
    @JSONField(serialize = false)
    private List<User> users;
    @ManyToMany(fetch = FetchType.EAGER)
    @JSONField(serialize = false)
    @JoinTable(name = "RolePermission", joinColumns = {@JoinColumn(name = "roleId")}, inverseJoinColumns = {@JoinColumn(name = "pid")})
    private List<Permission> permissions;

    //    重新toString，否则会有懒加载异常
    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isLocked=" + isLocked +
                ", permissions=" + permissions +
                '}';
    }

    public int getIsLocked() {
        return isLocked.getId();
    }

    public void setIsLocked(LockStatus isLocked) {
        this.isLocked = isLocked;
    }

}
