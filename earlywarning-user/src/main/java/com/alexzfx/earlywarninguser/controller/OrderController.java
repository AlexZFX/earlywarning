package com.alexzfx.earlywarninguser.controller;

import com.alexzfx.earlywarninguser.entity.InstOrder;
import com.alexzfx.earlywarninguser.entity.e.MaintainStatus;
import com.alexzfx.earlywarninguser.entity.request.RequestList;
import com.alexzfx.earlywarninguser.service.MsgPushService;
import com.alexzfx.earlywarninguser.service.OrderService;
import com.alexzfx.earlywarninguser.util.BaseResponse;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import static com.alexzfx.earlywarninguser.util.BaseResponse.EMPTY_SUCCESS_RESPONSE;

/**
 * Author : Alex
 * Date : 2018/4/22 16:42
 * Description :
 */
@RestController
public class OrderController {

    private final OrderService orderService;

    private final MsgPushService msgPushService;

    @Autowired
    public OrderController(OrderService orderService, MsgPushService msgPushService) {
        this.orderService = orderService;
        this.msgPushService = msgPushService;
    }


    @GetMapping("/admin/getOrderList")
    @RequiresRoles("admin")
    public BaseResponse<Page> getOrderListByUid(@RequestParam("uid") Integer uid, @RequestParam(value = "status", required = false) Integer status, @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page page = orderService.getOrderListByUidAndStatus(uid, status, pageable);
        return new BaseResponse<>(page);
    }

    @GetMapping("/getOrderList")
    public BaseResponse<Page> getOrderList(@PageableDefault(sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable) {
        Page orders = orderService.getOrderList(pageable);
        return new BaseResponse<>(orders);
    }

    @GetMapping("/getOrderDetail")
    public BaseResponse<InstOrder> getOrderDetail(@RequestParam("id") Long id) {
        InstOrder instOrder = orderService.getOrderDetail(id);
        return new BaseResponse<>(instOrder);
    }

    @PostMapping("/confirmOrder")
    @RequiresRoles(value = {"maintainer"})
    public BaseResponse confirmOrder(@RequestBody RequestList requestList) {
        msgPushService.pushConfirmAndSave(requestList.getOrderIds());
        return EMPTY_SUCCESS_RESPONSE;
    }

    @PostMapping("/fixingOrder")
    @RequiresRoles(value = {"maintainer"})
    public BaseResponse fixingOrder(@RequestBody RequestList requestList) {
        msgPushService.pushFixingAndSave(requestList.getOrderIds());
        return EMPTY_SUCCESS_RESPONSE;
    }

    @PostMapping("/finishOrder")
    @RequiresRoles(value = {"maintainer"})
    public BaseResponse finishOrder(@RequestBody RequestList requestList) {
        msgPushService.pushFinishAndSave(requestList.getOrderIds());
        return EMPTY_SUCCESS_RESPONSE;
    }

    @GetMapping("/getOrderByStatus")
    public BaseResponse<Page> getOrderByStatus(@RequestParam("status") Integer statusId, @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        MaintainStatus status = MaintainStatus.getStatusByNum(statusId);
        Page<InstOrder> instOrders = orderService.getOrderListByStatus(status, pageable);
        return new BaseResponse<>(instOrders);
    }

}
