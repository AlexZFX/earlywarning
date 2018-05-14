package com.alexzfx.earlywarninguser.shiro;

import com.alexzfx.earlywarninguser.entity.Permission;
import com.alexzfx.earlywarninguser.entity.Role;
import com.alexzfx.earlywarninguser.entity.User;
import com.alexzfx.earlywarninguser.exception.BaseException;
import com.alexzfx.earlywarninguser.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * Author : Alex
 * Date : 2018/3/5 13:04
 * Description :
 */
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private BaseException UnknownAccountError;

    @Autowired
    private UserService userService;


    //获取权限信息。
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username;
        if (principalCollection.getPrimaryPrincipal() instanceof String) {
            username = (String) principalCollection.getPrimaryPrincipal();
        } else {
            username = ((User) principalCollection.getPrimaryPrincipal()).getUsername();
        }
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
            throw new UnknownAccountException();
        }
        return new SimpleAuthenticationInfo(
                user, user.getPassword(), getName()
        );
    }
}
