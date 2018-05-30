package com.alexzfx.earlywarninguser.repository;

import com.alexzfx.earlywarninguser.entity.MachineData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Author : Alex
 * Date : 2018/4/16 19:13
 * Description :
 */
public interface MachineDataRepository extends JpaRepository<MachineData, Integer> {
    List<MachineData> findByMachineId(Integer machineId);
}
