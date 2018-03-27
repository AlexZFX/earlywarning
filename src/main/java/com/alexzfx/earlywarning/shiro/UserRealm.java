package com.alexzfx.earlywarning.shiro;

import com.alexzfx.earlywarning.entity.Permission;
import com.alexzfx.earlywarning.entity.Role;
import com.alexzfx.earlywarning.entity.User;
import com.alexzfx.earlywarning.exception.BaseException;
import com.alexzfx.earlywarning.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * Author : Alex
 * Date : 2018/3/5 13:04
 * Description :
 */
public class UserRealm extends AuthorizingRealm {


    @Resource
    private BaseException UnknownAccountError;

    @Autowired
    private UserService userService;


    //获取权限信息。
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = (String) principalCollection.getPrimaryPrincipal();
        //TODO 完善权限获取信息
        User user = userService.getUserByUsername(username);
        Set<String> roleNames = new HashSet<>();
        Set<String> permissionNames = new HashSet<>();
        for (Role role : user.getRoles()) {
            roleNames.add(role.getName());
            for (Permission permission : role.getPermissions()) {
                permissionNames.add(permission.getName());
            }
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setRoles(roleNames);
        info.setStringPermissions(permissionNames);
        return info;
    }

    //身份验证，验证用户登录的账号密码是否正确
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = (String) authenticationToken.getPrincipal();
        User user = userService.getUserByUsername(username);
        if (user == null) {
            throw UnknownAccountError;
        }
        return new SimpleAuthenticationInfo(
                user, user.getPassword(), getName()
        );
    }
}
