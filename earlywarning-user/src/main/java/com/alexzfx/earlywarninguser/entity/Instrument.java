package com.alexzfx.earlywarninguser.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Author : Alex
 * Date : 2018/4/8 17:23
 * Description : 仪器
 */
@Data
@Entity
public class Instrument implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//仪器id
    private String name;//仪器名称
    private String insType;//仪器型号
    private String param;//仪器参数
    @Column(columnDefinition = "varchar(32)")
    private String durableYears;// 使用年限
    @Column(columnDefinition = "varchar(2048)")
    private String description;//仪器描述
    private String picUrl;//图片链接
    private Integer thresholdValue;//告警阈值
    @Column(columnDefinition = "timestamp not null default current_timestamp")
    private Timestamp time;
    @JSONField(serialize = false)
    private boolean model = false; // 是否为模板
    @ManyToOne(targetEntity = InstCategory.class, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "category")
    private InstCategory category;
    @Transient
    @JSONField(serialize = false)
    private int cid; // 仅用于用户新建仪器时传来用作数据绑定使用
    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JSONField(serialize = false)
    @JoinColumn(name = "uid")
    private User creater;

    @Transient
    private User owner; // 管理员查看的时候附带用户信息


}
