package com.alexzfx.earlywarninguser.entity;

import com.alexzfx.earlywarninguser.entity.e.MaintainStatus;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Author : Alex
 * Date : 2018/4/17 14:23
 * Description : 用户消息，根据仪器id 获取仪器，来获取要发送给的用户。
 */
@Entity
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int uid;//发送给的用户的id
    @Column(columnDefinition = "timestamp not null default current_timestamp")
    private Timestamp createTime;
    private long orderId;
    private int machineId; // 仪器id
    private Integer data; // 仪器当时数据
    private Integer thresholdValue;// 当时阈值
    private String instName; // 仪器名称  可能影响数据的一致性。
    private MaintainStatus status = MaintainStatus.WAITCONFIRM; // 当时消息状态
    @Column(columnDefinition = "varchar(520)")
    private String content;
    private boolean isRead = false;//是否已读
    @Transient
    private User user;
    @Transient
    private User maintainer;
}
