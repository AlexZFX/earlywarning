package com.alexzfx.earlywarninguser.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Author : Alex
 * Date : 2018/4/22 10:04
 * Description :
 */
public interface MessageService {
    void readMsg(List<Integer> ids);

    Page getMessages(Pageable pageable);

    void readAllMsg();

    void deleteMessage(List<Integer> intIds);

    Long getUnReadNum();
}
