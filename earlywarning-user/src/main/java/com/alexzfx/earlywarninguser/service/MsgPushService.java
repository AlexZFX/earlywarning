package com.alexzfx.earlywarninguser.service;

import com.alexzfx.earlywarninguser.entity.InstOrder;
import com.alexzfx.earlywarninguser.entity.Instrument;
import com.alexzfx.earlywarninguser.entity.MachineData;
import com.alexzfx.earlywarninguser.entity.User;

import java.util.List;

/**
 * Author : Alex
 * Date : 2018/4/17 14:22
 * Description :
 */
public interface MsgPushService {
    void pushAndSave(Instrument instrument, MachineData machineData, User maintainer, InstOrder order);

    void pushConfirmAndSave(List<Long> orderIds);

    void pushFixingAndSave(List<Long> orderIds);

    void pushFinishAndSave(List<Long> orderIds);
}
