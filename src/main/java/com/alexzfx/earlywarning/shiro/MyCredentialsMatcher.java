package com.alexzfx.earlywarning.shiro;

import com.alexzfx.earlywarning.util.PasswordHash;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Author : Alex
 * Date : 2018/3/20 17:20
 * Description :
 * 自定义的凭证校验类，用于用自己的PasswordHash算法判断是否为正确密码
 */
public class MyCredentialsMatcher extends SimpleCredentialsMatcher {

    /**
     * @param authcToken 用户传入的密码参数
     * @param info       储存在本地的参数
     * @return
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken authcToken, AuthenticationInfo info) {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        String accountCredential = (String) getCredentials(info);
        String password = String.valueOf(token.getPassword());
        boolean bool = false;
        try {
            bool = PasswordHash.validatePassword(password, accountCredential);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return bool;
    }
}
