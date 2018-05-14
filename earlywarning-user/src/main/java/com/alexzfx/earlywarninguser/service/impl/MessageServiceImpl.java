package com.alexzfx.earlywarninguser.service.impl;

import com.alexzfx.earlywarninguser.entity.User;
import com.alexzfx.earlywarninguser.repository.MessageRepository;
import com.alexzfx.earlywarninguser.service.MessageService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Author : Alex
 * Date : 2018/4/22 10:04
 * Description :
 */
@Service
@Transactional
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void readMsg(List<Integer> ids) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        messageRepository.readMsgByIdsAndUid(ids, user.getId());
    }

    @Override
    public Page getMessages(Pageable pageable) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        return messageRepository.findByUid(user.getId(), pageable);
    }

    @Override
    public void readAllMsg() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        messageRepository.readAllMsgByUid(user.getId());
    }

    @Override
    public void deleteMessage(List<Integer> intIds) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        messageRepository.deleteByIdsAndUid(intIds, user.getId());
    }
}
