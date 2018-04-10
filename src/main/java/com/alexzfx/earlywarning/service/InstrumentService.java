package com.alexzfx.earlywarning.service;

import com.alexzfx.earlywarning.entity.InstCategory;

import java.util.List;

/**
 * Author : Alex
 * Date : 2018/4/9 10:56
 * Description :
 */
public interface InstrumentService {

    void createCategory(InstCategory instCategory);

    void deleteCategory(InstCategory instCategory);

    List<InstCategory> getAllCategory();
}
