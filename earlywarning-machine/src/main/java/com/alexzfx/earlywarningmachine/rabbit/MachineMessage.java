package com.alexzfx.earlywarningmachine.rabbit;

import lombok.Data;

import java.io.Serializable;

/**
 * Author : Alex
 * Date : 2018/4/12 13:36
 * Description :
 */
@Data
public class MachineMessage implements Serializable {
    int machineId;
    double data;

    public MachineMessage(int machineId, double data) {
        this.machineId = machineId;
        this.data = data;
    }

    public MachineMessage() {
    }
}
