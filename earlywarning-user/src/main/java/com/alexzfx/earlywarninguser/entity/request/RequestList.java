package com.alexzfx.earlywarninguser.entity.request;

import lombok.Data;

import java.util.List;

/**
 * Author : Alex
 * Date : 2018/4/22 10:03
 * Description :
 */
@Data
public class RequestList {
    private List<Integer> intIds;
    private List<Long> orderIds;
}
