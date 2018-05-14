package com.alexzfx.earlywarningmachine.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : Alex
 * Date : 2018/5/9 17:36
 * Description :
 */
@Data
public class JobList {
    private List<Integer> workerIds;
    private List<Integer> pausedIds;

    public JobList() {
        workerIds = new ArrayList<>();
        pausedIds = new ArrayList<>();
    }
}
