package com.alexzfx.earlywarninguser.entity.e;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Author : Alex
 * Date : 2018/4/18 10:27
 * Description :
 */
public enum MaintainStatus {
    WAITCONFIRM(0),
    CONFIRMED(1),
    FIXING(2),
    FINISHED(3);

    private int id;

    MaintainStatus(int id) {
        this.id = id;
    }

    @JSONField(serialize = false)
    public static MaintainStatus getStatusByNum(int id) {
        switch (id) {
            case 0:
                return WAITCONFIRM;
            case 1:
                return CONFIRMED;
            case 2:
                return FIXING;
            case 3:
                return FINISHED;
            default:
                return WAITCONFIRM;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
