package com.alexzfx.earlywarninguser.util.WSUtil;

import com.alexzfx.earlywarninguser.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.web.socket.messaging.SessionConnectEvent;

/**
 * Author : Alex
 * Date : 2018/4/7 11:15
 * Description :
 */
public class STOMPConnectEventListener implements ApplicationListener<SessionConnectEvent> {

    @Autowired
    private SocketSessionRegistry socketSessionRegistry;

    @Override
    public void onApplicationEvent(SessionConnectEvent sessionConnectEvent) {
        //获取消息头
//        StompHeaderAccessor head = StompHeaderAccessor.wrap(sessionConnectEvent.getMessage());
//        String sessionId = head.getSessionId();
        Subject subject = SecurityUtils.getSubject();
        String sessionId = (String) subject.getSession().getId();
        String username = ((User) subject.getPrincipal()).getUsername();
        socketSessionRegistry.registerSessionId(username, sessionId);
    }
}

