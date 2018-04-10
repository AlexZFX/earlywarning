package com.alexzfx.earlywarning.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * Author : Alex
 * Date : 2018/4/8 17:23
 * Description : 仪器
 */
@Data
@Entity
public class Instrument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;//仪器id
    private String name;//仪器名称
    private String description;//仪器描述
    private String picUrl;//图片链接
    private String thresholdValue;//告警阈值
    @ManyToOne(targetEntity = InstCategory.class, fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.ALL})
    @JoinColumn(name = "category")
    private InstCategory category;
}
