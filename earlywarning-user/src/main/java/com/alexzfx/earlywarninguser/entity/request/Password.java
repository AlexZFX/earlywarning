package com.alexzfx.earlywarninguser.entity.request;

import lombok.Data;

/**
 * Author : Alex
 * Date : 2018/4/30 20:31
 * Description :
 */
@Data
public class Password {
    private String newPassword;
    private String oldPassword;
}
