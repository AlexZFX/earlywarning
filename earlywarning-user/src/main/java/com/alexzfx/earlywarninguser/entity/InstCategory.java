package com.alexzfx.earlywarninguser.entity;

import com.alexzfx.earlywarninguser.util.GsonUtil.GsonIgnore;
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
    @Column(columnDefinition = "varchar(2048)")
    private String description;
    @OneToMany(targetEntity = Instrument.class, fetch = FetchType.LAZY, mappedBy = "category")
    @GsonIgnore
    private List<Instrument> instruments;
}
