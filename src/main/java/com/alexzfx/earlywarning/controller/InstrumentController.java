package com.alexzfx.earlywarning.controller;

import com.alexzfx.earlywarning.entity.InstCategory;
import com.alexzfx.earlywarning.service.InstrumentService;
import com.alexzfx.earlywarning.util.BaseResponse;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import static com.alexzfx.earlywarning.util.BaseResponse.EMPTY_SUCCESS_RESPONSE;

/**
 * Author : Alex
 * Date : 2018/4/9 11:23
 * Description :
 */
@Controller
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

    @GetMapping("/getCategories")
    public BaseResponse<List> getCategory() {
        List<InstCategory> categories = instrumentService.getAllCategory();
        return new BaseResponse(categories);
    }


}
