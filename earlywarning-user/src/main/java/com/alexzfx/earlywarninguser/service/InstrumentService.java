package com.alexzfx.earlywarninguser.service;

import com.alexzfx.earlywarninguser.entity.InstCategory;
import com.alexzfx.earlywarninguser.entity.Instrument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

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

    Page<Instrument> getInstrumentByCid(int cid, Pageable pageable);

    void modifyCategory(InstCategory instCategory);

    Page<Instrument> getModelInstrumentByCid(int cid, Pageable pageable);

    Instrument getInstrumentById(int id);

    void createModelInstrument(Instrument instrument);

    Page<Instrument> getUserInstrument(Pageable pageable, String keyWord);

    Integer createInstrument(Instrument instrument);

    void modifyInstrument(Instrument instrument);

    Page<Instrument> getInstruments(Integer uid, String keyWord, Pageable pageable);

    String uploadInstPic(MultipartFile file);

    void deleteInstrumentById(Integer id);
}
