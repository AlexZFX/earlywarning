package com.alexzfx.earlywarninguser.util.WSUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectEvent;

/**
 * Author : Alex
 * Date : 2018/4/7 11:15
 * Description :
 */
@Slf4j
public class STOMPConnectEventListener implements ApplicationListener<SessionConnectEvent> {

    @Autowired
    private SocketSessionRegistry socketSessionRegistry;

    @Override
    public void onApplicationEvent(SessionConnectEvent sessionConnectEvent) {
//        获取消息头
        StompHeaderAccessor head = StompHeaderAccessor.wrap(sessionConnectEvent.getMessage());
        log.info(head.toString());
        String sessionId = head.getSessionId();
        String username = head.getNativeHeader("username").get(0);
        log.info("用户" + username + "连入socket" + ":" + sessionId);
        socketSessionRegistry.registerSessionId(username, sessionId);
    }
}

