package com.alexzfx.earlywarninguser.entity;

import com.alexzfx.earlywarninguser.entity.e.MaintainStatus;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Author : Alex
 * Date : 2018/4/20 9:45
 * Description : 仪器维修的订单。在仪器出现问题时自动生成
 */
@Entity
@Data
public class InstOrder {
    @Id
    private Long id;
    private MaintainStatus maintainStatus = MaintainStatus.WAITCONFIRM;
    @ManyToOne(targetEntity = Instrument.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "instId", referencedColumnName = "id")
    private Instrument instrument; // 对应仪器
    @Column(columnDefinition = "timestamp not null default current_timestamp")
    private Timestamp createTime;//开始时间
    private Timestamp confirmTime;//确认时间
    private Timestamp fixTime;//维修时间
    private Timestamp finishTime;//完成时间
    @JSONField(serialize = false)
    private Integer ownerId;
    @JSONField(serialize = false)
    private Integer maintainerId;
    @Transient
    private User owner; // 仪器所有者
    //TODO 谁能看到谁的信息
    @Transient
    private User maintainer;//维修人员

    public int getMaintainStatus() {
        return maintainStatus.getId();
    }

    public void setMaintainStatus(MaintainStatus maintainStatus) {
        this.maintainStatus = maintainStatus;
    }

    @JSONField(serialize = false)
    public MaintainStatus getEnumMaintainStatus() {
        return this.maintainStatus;
    }
}
