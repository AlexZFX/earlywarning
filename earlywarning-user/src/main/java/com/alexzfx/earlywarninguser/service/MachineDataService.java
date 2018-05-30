package com.alexzfx.earlywarninguser.service;

import com.alexzfx.earlywarninguser.entity.MachineData;

import java.util.List;

/**
 * Author : Alex
 * Date : 2018/4/15 19:44
 * Description : 处理用户仪器发来数据的service。
 */
public interface MachineDataService {
    void handleData(MachineData machineData);

    List<MachineData> getAllData();

    List<MachineData> getDataByMachineId(Integer machineId);
}
