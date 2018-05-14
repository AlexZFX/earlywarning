package com.alexzfx.earlywarninguser.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Author : Alex
 * Date : 2018/4/8 19:05
 * Description : 仪器分类 加上了可序列化的接口
 */

@Data
@Entity
public class InstCategory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @Column(columnDefinition = "varchar(2048)")
    private String description;
    @OneToMany(targetEntity = Instrument.class, fetch = FetchType.LAZY, mappedBy = "category")
    @JSONField(serialize = false)
    private List<Instrument> instruments;

    @Override
    public String toString() {
        return "InstCategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
