package com.alexzfx.earlywarning.controller;

import com.alexzfx.earlywarning.util.WSUtil.SocketSessionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * Author : Alex
 * Date : 2018/4/6 16:20
 * Description :
 */
@Controller
public class WebSocketController {

    private final SocketSessionRegistry socketSessionRegistry;

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketController(SocketSessionRegistry socketSessionRegistry, SimpMessagingTemplate messagingTemplate) {
        this.socketSessionRegistry = socketSessionRegistry;
        this.messagingTemplate = messagingTemplate;
    }


}
