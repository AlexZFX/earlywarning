package com.alexzfx.earlywarning.entity.e;

import com.alexzfx.earlywarning.exception.BaseException;
import com.alexzfx.earlywarning.util.GsonUtil.GsonEnum;

/**
 * Author : Alex
 * Date : 2018/3/21 10:36
 * Description :
 */
public enum LockStatus implements GsonEnum<LockStatus> {
    LOCKED(0),
    UNLOCKED(1);

    LockStatus(int id) {
        this.id = id;
    }

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String serialize() {
        return String.valueOf(this.id);
    }

    @Override
    public LockStatus deserialize(String s) {
        return parse(s);
    }

    @Override
    public LockStatus parse(String type) {
        switch (Integer.valueOf(type)) {
            case 0:
                return LOCKED;
            case 1:
                return UNLOCKED;
            default:
                throw new BaseException();
        }
    }


}
