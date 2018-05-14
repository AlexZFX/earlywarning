package com.alexzfx.earlywarninguser.entity.e;

/**
 * Author : Alex
 * Date : 2018/3/21 10:36
 * Description :
 */
public enum LockStatus {
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


}
