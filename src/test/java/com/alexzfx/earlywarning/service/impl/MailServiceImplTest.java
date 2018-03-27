package com.alexzfx.earlywarning.service.impl;

import com.alexzfx.earlywarning.service.MailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Author : Alex
 * Date : 2018/3/22 10:08
 * Description :\
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MailServiceImplTest {


    @Autowired
    private MailService mailService;


    @Test
    public void sendAuthMail() {
        mailService.sendAuthMail("1079911968@qq.com");
    }
}