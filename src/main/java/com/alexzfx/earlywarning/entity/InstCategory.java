package com.alexzfx.earlywarning.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * Author : Alex
 * Date : 2018/4/8 19:05
 * Description : 仪器分类
 */
@Data
@Entity
public class InstCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @OneToMany(targetEntity = Instrument.class, fetch = FetchType.EAGER, mappedBy = "category")
    private List<Instrument> instruments;
}
