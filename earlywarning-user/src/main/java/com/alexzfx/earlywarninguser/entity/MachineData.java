package com.alexzfx.earlywarninguser.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Author : Alex
 * Date : 2018/4/12 13:36
 * Description :
 */

@Entity
@Data
public class MachineData implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private int machineId;
    private int data;
}
