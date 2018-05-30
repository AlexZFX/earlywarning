package com.alexzfx.earlywarninguser.util.WSUtil;

import com.alexzfx.earlywarninguser.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
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


    //拿不到用户信息，不好清除
    @Override
    public void onApplicationEvent(SessionDisconnectEvent sessionDisconnectEvent) {

        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if (user == null) {
            log.info("SessionDisconnectEvent user null");
        } else {
            log.info("SessionDisconnectEvent user not null");
        }

//        log.info("用户" + user.getUsername() + "断开socket" + ": " + sessionDisconnectEvent.getSessionId());
//        log.info("用户" + user.getUsername() + "断开socket" + ": " + sessionDisconnectEvent.getSessionId());
//        socketSessionRegistry.unregisterSessionId(user.getUsername(), sessionDisconnectEvent.getSessionId());
    }
}
