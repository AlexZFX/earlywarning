package com.alexzfx.earlywarninguser.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

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
    @JSONField(serialize = false)
    private Integer id;
    @JSONField(serialize = false)
    private int machineId;
    private int data;
    @Column(columnDefinition = "timestamp not null default current_timestamp")
    private Timestamp time;
}
