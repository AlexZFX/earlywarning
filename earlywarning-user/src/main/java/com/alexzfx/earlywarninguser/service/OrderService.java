package com.alexzfx.earlywarninguser.service;

import com.alexzfx.earlywarninguser.entity.InstOrder;
import com.alexzfx.earlywarninguser.entity.e.MaintainStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Author : Alex
 * Date : 2018/4/22 16:51
 * Description :
 */
public interface OrderService {
    Page getOrderList(Pageable pageable);

    InstOrder getOrderDetail(Long id);

    Page<InstOrder> getOrderListByStatus(MaintainStatus maintainStatus, Pageable pageable);

    Page getOrderListByUidAndStatus(Integer uid, Integer status, Pageable pageable);
}
