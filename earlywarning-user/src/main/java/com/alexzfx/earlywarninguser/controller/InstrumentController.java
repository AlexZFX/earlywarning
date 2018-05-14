package com.alexzfx.earlywarninguser.controller;

import com.alexzfx.earlywarninguser.entity.InstCategory;
import com.alexzfx.earlywarninguser.entity.Instrument;
import com.alexzfx.earlywarninguser.service.InstrumentService;
import com.alexzfx.earlywarninguser.util.BaseResponse;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.alexzfx.earlywarninguser.util.BaseResponse.EMPTY_SUCCESS_RESPONSE;

/**
 * Author : Alex
 * Date : 2018/4/9 11:23
 * Description :
 */
@RestController
public class InstrumentController {
    private final InstrumentService instrumentService;

    @Autowired
    public InstrumentController(InstrumentService instrumentService) {
        this.instrumentService = instrumentService;
    }

    @PostMapping("/admin/createCategory")
    @RequiresRoles(value = {"admin"})
    public BaseResponse createCategory(@RequestBody InstCategory instCategory) {
        instrumentService.createCategory(instCategory);
        return EMPTY_SUCCESS_RESPONSE;
    }

    @PostMapping("/admin/deleteCategory")
    @RequiresRoles(value = {"admin"})
    public BaseResponse deleteCategory(@RequestBody InstCategory instCategory) {
        instrumentService.deleteCategory(instCategory);
        return EMPTY_SUCCESS_RESPONSE;
    }

    @PostMapping("/admin/modifyCategory")
    @RequiresRoles(value = {"admin"})
    public BaseResponse modifyCategory(@RequestBody InstCategory instCategory) {
        instrumentService.modifyCategory(instCategory);
        return EMPTY_SUCCESS_RESPONSE;
    }

    @PostMapping("/admin/createInstrument")
    @RequiresRoles(value = {"admin"})
    public BaseResponse createModelInstrument(@RequestBody Instrument instrument) {
        instrumentService.createModelInstrument(instrument);
        return EMPTY_SUCCESS_RESPONSE;
    }


    @GetMapping("/admin/getInstruments")
    @RequiresRoles(value = {"admin"})
    public BaseResponse<Page> getInstruments(@RequestParam(required = false) Integer uid, @RequestParam(required = false) String keyWord, @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Instrument> page = instrumentService.getInstruments(uid, keyWord, pageable);
        return new BaseResponse<>(page);
    }

    @PostMapping("/createInstrument")
    public BaseResponse<Integer> createInstrument(@RequestBody Instrument instrument) {
        //TODO  需要返回一个id及接口。
        Integer id = instrumentService.createInstrument(instrument);
        return new BaseResponse<>(id);
    }

    @PostMapping("/modifyInstrument")
    public BaseResponse modifyInstrument(@RequestBody Instrument instrument) {
        instrumentService.modifyInstrument(instrument);
        return EMPTY_SUCCESS_RESPONSE;
    }

    @PostMapping("/deleteInstrument")
    public BaseResponse deleteInstrument(@RequestParam("id") Integer id) {
        instrumentService.deleteInstrumentById(id);
        return EMPTY_SUCCESS_RESPONSE;
    }

    @PostMapping("/uploadInstPic")
    public BaseResponse<String> uploadInstrument(@RequestParam("file") MultipartFile file) {
        //TODO instfile
        String picUrl = instrumentService.uploadInstPic(file);
        return new BaseResponse<>(picUrl);
    }

    @GetMapping("/getCategories")
    public BaseResponse<List<InstCategory>> getCategory() {
        List<InstCategory> categories = instrumentService.getAllCategory();
        return new BaseResponse<>(categories);
    }

    @GetMapping("/getUserInstrument")
    public BaseResponse<Page> getUserInstrument(@PageableDefault(direction = Sort.Direction.ASC) Pageable pageable, @RequestParam(value = "keyWord", required = false) String keyWord) {
        Page<Instrument> instruments = instrumentService.getUserInstrument(pageable, keyWord);
        return new BaseResponse<>(instruments);
    }

    //查看类型对应的仪器
    @GetMapping("/getInstrumentByCid")
    public BaseResponse<Page> getInstrumentByCid(@RequestParam(name = "cid") int cid, @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Instrument> instruments = instrumentService.getInstrumentByCid(cid, pageable);
        return new BaseResponse<>(instruments);
    }


    //用户&管理员
    @GetMapping("/getModelInstrumentByCid")
    public BaseResponse<Page> getModelInstrumentByCid(@RequestParam(name = "cid") int cid, @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Instrument> instruments = instrumentService.getModelInstrumentByCid(cid, pageable);
        return new BaseResponse<>(instruments);
    }


    @GetMapping("/getInstrumentById")
    public BaseResponse<Instrument> getInstrumentById(@RequestParam(name = "id") int id) {
        Instrument instrument = instrumentService.getInstrumentById(id);
        return new BaseResponse<>(instrument);
    }


}
