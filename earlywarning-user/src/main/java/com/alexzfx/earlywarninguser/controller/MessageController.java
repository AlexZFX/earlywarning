package com.alexzfx.earlywarninguser.controller;

import com.alexzfx.earlywarninguser.entity.request.RequestList;
import com.alexzfx.earlywarninguser.service.MessageService;
import com.alexzfx.earlywarninguser.util.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.alexzfx.earlywarninguser.util.BaseResponse.EMPTY_SUCCESS_RESPONSE;

/**
 * Author : Alex
 * Date : 2018/4/22 9:59
 * Description :
 */
@RestController
public class MessageController {

    private final MessageService messageService;


    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/readMessage")
    public BaseResponse readMessage(@RequestBody RequestList requestList) {
        messageService.readMsg(requestList.getIntIds());
        return EMPTY_SUCCESS_RESPONSE;
    }

    @PostMapping("/readAllMessage")
    public BaseResponse readAllMessage() {
        messageService.readAllMsg();
        return EMPTY_SUCCESS_RESPONSE;
    }

    @GetMapping("/getMessages")
    public BaseResponse<Page> getMessages(@PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page page = messageService.getMessages(pageable);
        return new BaseResponse<>(page);
    }

    @GetMapping("/getUnReadNum")
    public BaseResponse<Long> getUnReadNum() {
        Long num = messageService.getUnReadNum();
        return new BaseResponse<>(num);
    }

    @PostMapping("/deleteMessage")
    public BaseResponse deleteMessage(@RequestBody RequestList requestList) {
        messageService.deleteMessage(requestList.getIntIds());
        return EMPTY_SUCCESS_RESPONSE;
    }


}
