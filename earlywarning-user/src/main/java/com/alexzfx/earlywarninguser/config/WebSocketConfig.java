package com.alexzfx.earlywarninguser.config;

import com.alexzfx.earlywarninguser.util.WSUtil.SocketSessionRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Author : Alex
 * Date : 2018/4/6 14:23
 * Description :
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // webSocket被设置为websocket的端点，客户端需要注册这个端点进行链接，withSockJS允许客户端利用sockjs进行浏览器兼容性处理
        registry.addEndpoint("/webSocket").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //设置订阅的Broker名称（服务器广播消息的基础路径）（即客户端订阅的地址的前缀）
        registry.enableSimpleBroker("/topic", "/maintainer");
        //客户端请求前缀（订阅消息的基础路径）（服务端接受地址的前缀，即客户端发送地址的前缀）
        //例如客户端发送消息的目的地为/app/sendTest，则对应控制层@MessageMapping(“/sendTest”)
        //客户端订阅主题的目的地为/app/subscribeTest，则对应控制层@SubscribeMapping(“/subscribeTest”)
        registry.setApplicationDestinationPrefixes("/app");
        //点对点使用的订阅前缀(客户端订阅路径上会体现出来，不设置的话，默认为"/user/")
        registry.setUserDestinationPrefix("/user/");
    }

    @Bean
    public SocketSessionRegistry socketSessionRegistry() {
        return new SocketSessionRegistry();
    }
}
