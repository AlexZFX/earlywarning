package com.alexzfx.earlywarninguser.controller;

import com.alexzfx.earlywarninguser.entity.MachineData;
import com.alexzfx.earlywarninguser.service.MachineDataService;
import com.alexzfx.earlywarninguser.util.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : Alex
 * Date : 2018/5/18 18:34
 * Description :
 */
@RestController
public class DataController {

    private final MachineDataService dataService;

    @Autowired
    public DataController(MachineDataService dataService) {
        this.dataService = dataService;
    }
//
//    @GetMapping("/getAllData")
//    public BaseResponse<List<MachineData>> getAllData() {
//        List<MachineData> data =  dataService.getAllData();
//        return new BaseResponse<>(data);
//    }

    @GetMapping("/getDataByMachineId")
    public BaseResponse<List> getAllData(@RequestParam("machineId") Integer machineId) {
        List<MachineData> data = dataService.getDataByMachineId(machineId);
        List<double[]> formatData = new ArrayList<>();
        double[] oneData = new double[2];
        for (MachineData datum : data) {
            oneData[0] = (double) datum.getTime().getTime();
            oneData[1] = datum.getData();
            formatData.add(oneData.clone());
        }
        return new BaseResponse<>(formatData);
    }

}
