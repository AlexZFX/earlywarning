package com.alexzfx.earlywarninguser.util.WSUtil;

import org.apache.shiro.util.Assert;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Author : Alex
 * Date : 2018/4/7 11:38
 * Description : 用户session的记录类，参考  https://yq.aliyun.com/articles/476079
 */
public class SocketSessionRegistry {

    //用来存储sessionid，一个用户可能对应多个session，多点登录
    private final ConcurrentHashMap<String, Set<String>> userSessionIds = new ConcurrentHashMap<>();

    public SocketSessionRegistry() {
    }

    /**
     * 根据用户名获取sessionids
     *
     * @param username
     * @return
     */
    public Set<String> getSessionIds(String username) {
        Set set = userSessionIds.get(username);
        return set == null ? Collections.emptySet() : set;
    }

    public ConcurrentHashMap<String, Set<String>> getAllSessionIds() {
        return this.userSessionIds;
    }

    public void registerSessionId(String username, String sessionId) {
        Assert.notNull(username);
        Assert.notNull(sessionId);
        synchronized (this) {
            Set<String> set = this.userSessionIds.get(username);
            if (set == null) {
                set = new CopyOnWriteArraySet();
                this.userSessionIds.put(username, set);
            }
            set.add(sessionId);
        }
    }

    public void unregisterSessionId(String username, String sessionId) {
        Assert.notNull(username);
        Assert.notNull(sessionId);
        synchronized (this) {
            Set set = userSessionIds.get(username);
            //set不为空时删除其中的sessionId,再判断是否为空，为空则删掉Map中的该set
            if (set != null && set.remove(sessionId) && set.isEmpty()) {
                this.userSessionIds.remove(username);
            }
        }
    }
}
