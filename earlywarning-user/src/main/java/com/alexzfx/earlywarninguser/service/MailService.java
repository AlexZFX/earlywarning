package com.alexzfx.earlywarninguser.service;

import com.alexzfx.earlywarninguser.entity.InstOrder;
import com.alexzfx.earlywarninguser.entity.Instrument;
import com.alexzfx.earlywarninguser.entity.MachineData;
import com.alexzfx.earlywarninguser.entity.User;

/**
 * Author : Alex
 * Date : 2018/3/23 18:47
 * Description :
 */
public interface MailService {
    void sendAuthMail(String mail);

    void validAuthMail(int uid, String authCode);

    void sendWarningEmail(Instrument instrument, MachineData machineData, User maintainer, InstOrder order);
}
