package com.alexzfx.earlywarning.service.impl;

import com.alexzfx.earlywarning.entity.InstCategory;
import com.alexzfx.earlywarning.repository.CategoryRepository;
import com.alexzfx.earlywarning.repository.InstrumentRepository;
import com.alexzfx.earlywarning.service.InstrumentService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author : Alex
 * Date : 2018/4/9 10:56
 * Description :
 */
@Service
public class InstrumentServiceImpl implements InstrumentService {

    private final CategoryRepository categoryRepository;

    private final InstrumentRepository instrumentRepository;

    public InstrumentServiceImpl(CategoryRepository categoryRepository, InstrumentRepository instrumentRepository) {
        this.categoryRepository = categoryRepository;
        this.instrumentRepository = instrumentRepository;
    }

    @Override
    public void createCategory(InstCategory instCategory) {
        categoryRepository.save(instCategory);
    }

    @Override
    public void deleteCategory(InstCategory instCategory) {
        categoryRepository.delete(instCategory);
    }

    @Override
    public List<InstCategory> getAllCategory() {
        return categoryRepository.findAll();
    }
}
