package com.alexzfx.earlywarninguser.util.WSUtil;

import com.alexzfx.earlywarninguser.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 * Author : Alex
 * Date : 2018/4/21 20:45
 * Description :
 */
@Slf4j
public class SessionDisconnectListener implements ApplicationListener<SessionDisconnectEvent> {

    @Autowired
    private SocketSessionRegistry socketSessionRegistry;


    @Override
    public void onApplicationEvent(SessionDisconnectEvent sessionDisconnectEvent) {
        User user = (User) SecurityUtils.getSubject().getPrincipals();
        log.info("用户" + user.getUsername() + "断开socket" + ": " + sessionDisconnectEvent.getSessionId());
        socketSessionRegistry.unregisterSessionId(user.getUsername(), sessionDisconnectEvent.getSessionId());
    }
}
