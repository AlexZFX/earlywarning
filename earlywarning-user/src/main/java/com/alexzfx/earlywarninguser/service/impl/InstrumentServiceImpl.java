package com.alexzfx.earlywarninguser.service.impl;

import com.alexzfx.earlywarninguser.entity.InstCategory;
import com.alexzfx.earlywarninguser.entity.Instrument;
import com.alexzfx.earlywarninguser.entity.User;
import com.alexzfx.earlywarninguser.repository.CategoryRepository;
import com.alexzfx.earlywarninguser.repository.InstrumentRepository;
import com.alexzfx.earlywarninguser.service.InstrumentService;
import org.apache.shiro.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    //根据用户id和分类id查找仪器
    @Override
    public Page<Instrument> getInstrumentByCid(int cid, Pageable pageable) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        return instrumentRepository.findByCategoryAndCreater(categoryRepository.getOne(cid), user, pageable);
    }

    @Override
    public void modifyCategory(InstCategory instCategory) {
        categoryRepository.save(instCategory);
    }

    //根据是否为模板查找，供管理员使用。
    @Override
    public Page<Instrument> getModelInstrumentByCid(int cid, Pageable pageable) {
        return instrumentRepository.findByCategoryAndModel(categoryRepository.getOne(cid), true, pageable);
    }

    @Override
    public Instrument getInstrumentById(int id) {
        return instrumentRepository.getOne(id);
    }

    @Override
    public void createModelInstrument(Instrument instrument) {
        instrument.setCreater((User) SecurityUtils.getSubject().getPrincipal());
        instrument.setModel(true);
        instrumentRepository.save(instrument);
    }
}
