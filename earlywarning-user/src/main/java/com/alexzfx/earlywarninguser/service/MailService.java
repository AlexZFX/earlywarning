package com.alexzfx.earlywarninguser.service;

/**
 * Author : Alex
 * Date : 2018/3/23 18:47
 * Description :
 */
public interface MailService {
    void sendAuthMail(String mail);
    void validAuthMail(int uid,String authCode);
}
