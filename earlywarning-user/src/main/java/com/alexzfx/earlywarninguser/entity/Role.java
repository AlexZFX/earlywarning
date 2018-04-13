package com.alexzfx.earlywarninguser.entity;

import com.alexzfx.earlywarninguser.entity.e.LockStatus;
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
public class Role implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String name;
    private String description;
    @Column(columnDefinition = "int not null default 1")
    private LockStatus isLocked = LockStatus.UNLOCKED;
    @ManyToMany
    @JoinTable(name = "UserRole", joinColumns = {@JoinColumn(name = "roleId")}, inverseJoinColumns = {@JoinColumn(name = "uid")})
    private List<User> users;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "RolePermission", joinColumns = {@JoinColumn(name = "roleId")}, inverseJoinColumns = {@JoinColumn(name = "pid")})
    private List<Permission> permissions;

    //重新toString，否则会有懒加载异常
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
}
