package com.alexzfx.earlywarninguser.service.impl;

import com.alexzfx.earlywarninguser.entity.InstCategory;
import com.alexzfx.earlywarninguser.entity.Instrument;
import com.alexzfx.earlywarninguser.entity.User;
import com.alexzfx.earlywarninguser.entity.e.MaintainStatus;
import com.alexzfx.earlywarninguser.exception.BaseException;
import com.alexzfx.earlywarninguser.repository.CategoryRepository;
import com.alexzfx.earlywarninguser.repository.InstOrderRepository;
import com.alexzfx.earlywarninguser.repository.InstrumentRepository;
import com.alexzfx.earlywarninguser.repository.UserRepository;
import com.alexzfx.earlywarninguser.service.InstrumentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Author : Alex
 * Date : 2018/4/9 10:56
 * Description :
 */
@Service
@Transactional
@Slf4j
public class InstrumentServiceImpl implements InstrumentService {

    private final CategoryRepository categoryRepository;

    private final InstrumentRepository instrumentRepository;

    private final UserRepository userRepository;

    private final InstOrderRepository instOrderRepository;

    private final String RootPath;

    private final BaseException PermissionDenied;

    private final BaseException NotFoundError;

    private final BaseException DeleteFailError;

    private final BaseException UnknownAccountError;

    private final BaseException FileTransError;

    public InstrumentServiceImpl(CategoryRepository categoryRepository, InstrumentRepository instrumentRepository, UserRepository userRepository, InstOrderRepository instOrderRepository, String RootPath, BaseException PermissionDenied, BaseException NotFoundError, BaseException DeleteFailError, BaseException UnknownAccountError, BaseException FileTransError) {
        this.categoryRepository = categoryRepository;
        this.instrumentRepository = instrumentRepository;
        this.userRepository = userRepository;
        this.instOrderRepository = instOrderRepository;
        this.RootPath = RootPath;
        this.PermissionDenied = PermissionDenied;
        this.NotFoundError = NotFoundError;
        this.DeleteFailError = DeleteFailError;
        this.UnknownAccountError = UnknownAccountError;
        this.FileTransError = FileTransError;
    }

    @Override
    public void createCategory(InstCategory instCategory) {
        categoryRepository.save(instCategory);
    }

    @Override
    public void deleteCategory(InstCategory instCategory) {
        Long num = instrumentRepository.countByCategory(instCategory);
        if (num != 0) {
            log.error("分类中仍有仪器，无法删除");
            throw DeleteFailError;
        }
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
        try {
            User user = (User) SecurityUtils.getSubject().getPrincipal();
            Instrument instrument = instrumentRepository.findById(id).get();
            if (user.getRoleNames().contains("admin")) {
                User owner = instrument.getCreater();
                instrument.setOwner(owner);
                return instrument;
            } else {
                if (!user.getUsername().equals(instrument.getCreater().getUsername())) {
                    log.error("不是用户本人的仪器，删除失败");
                    throw PermissionDenied;
                }
                return instrument;
            }
        } catch (EntityNotFoundException | NoSuchElementException e) {
            throw NotFoundError;
        }
    }


    @Override
    public void createModelInstrument(Instrument instrument) {
        instrument.setCreater((User) SecurityUtils.getSubject().getPrincipal());
        instrument.setModel(true);
        instrument.setCategory(categoryRepository.getOne(instrument.getCid()));
        instrument.setPicUrl(instrument.getPicUrl());
        instrumentRepository.save(instrument);
    }

    @Override
    public Page<Instrument> getUserInstrument(Pageable pageable, String keyWord) {
        User creater = (User) SecurityUtils.getSubject().getPrincipal();
        if (keyWord == null) {
            return instrumentRepository.findByCreater(creater, pageable);
        } else {
            return instrumentRepository.findByCreaterAndNameLike(creater, "%" + keyWord + "%", pageable);
        }
    }

    @Override
    public Integer createInstrument(Instrument instrument) {
        instrument.setId(null);
        instrument.setCreater((User) SecurityUtils.getSubject().getPrincipal());
        instrument.setCategory(categoryRepository.getOne(instrument.getCid()));
        instrument.setModel(false);
        instrument.setPicUrl(instrument.getPicUrl());
        instrument = instrumentRepository.save(instrument);
        return instrument.getId();
    }

    @Override
    public void modifyInstrument(Instrument instrument) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Instrument oldIns = instrumentRepository.getOne(instrument.getId());
        if (!user.getId().equals(oldIns.getCreater().getId())) {
            log.error("非用户本人仪器，修改仪器信息失败");
            throw PermissionDenied;
        }
        oldIns.setInsType(instrument.getInsType());
        oldIns.setDurableYears(instrument.getDurableYears());
        oldIns.setParam(instrument.getParam());
        oldIns.setDescription(instrument.getDescription());
        oldIns.setThresholdValue(instrument.getThresholdValue());
        oldIns.setName(instrument.getName());
        instrumentRepository.save(oldIns);
    }

    @Override
    public Page<Instrument> getInstruments(Integer uid, String keyWord, Pageable pageable) {
        Page<Instrument> page;
        User user;
        if (uid == null && keyWord == null) {
            page = instrumentRepository.findByModel(false, pageable);
        } else if (uid == null) {
            page = instrumentRepository.findByModelAndNameLike(false, "%" + keyWord + "%", pageable);
        } else if (keyWord == null) {
            try {
                user = userRepository.findById(uid).get();
            } catch (NoSuchElementException e) {
                throw UnknownAccountError;
            }
            page = instrumentRepository.findByCreater(user, pageable);
        } else {
            try {
                user = userRepository.findById(uid).get();
            } catch (NoSuchElementException e) {
                throw UnknownAccountError;
            }
            page = instrumentRepository.findByCreaterAndNameLike(user, keyWord, pageable);
        }
        return page;
    }

    @Override
    public String uploadInstPic(MultipartFile file) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        String fileName = "instPic/" + user.getId() + System.currentTimeMillis() + ".png";
        File pic = new File(RootPath + fileName);
        if (!pic.getParentFile().isDirectory()) {
            pic.getParentFile().mkdirs();
        }
        try {
            file.transferTo(pic.getAbsoluteFile());
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
            throw FileTransError;
        }
        return fileName;
    }

    @Override
    public void deleteInstrumentById(Integer id) {
        try {
            Instrument instrument = instrumentRepository.findById(id).get();
            User user = (User) SecurityUtils.getSubject().getPrincipal();
            if (instrument.getCreater().getId() != user.getId()) {
                throw PermissionDenied;
            }
            Long oid = instOrderRepository.findIsFixing(id, MaintainStatus.FINISHED);
            if (oid != null) {
                throw DeleteFailError;
            }
            instrumentRepository.delete(instrument);
        } catch (NoSuchElementException e) {
            log.error(e.getLocalizedMessage());
            throw new BaseException(500, "仪器不存在");
        }
    }
}
