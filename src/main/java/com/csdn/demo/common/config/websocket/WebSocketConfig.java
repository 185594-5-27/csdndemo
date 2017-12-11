package com.csdn.demo.common.config.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/*
* 类描述：
* @auther linzf
* @create 2017/8/8 0008 
*/
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");// /users 默认通知
        config.setApplicationDestinationPrefixes("/app");
        //设置前缀  默认是user 可以修改  点对点时使用
        config.setUserDestinationPrefix("/ricky/");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ricky-websocket").withSockJS();
    }


    @Bean
    public SocketSessionRegistry SocketSessionRegistry(){
        return new SocketSessionRegistry();
    }
    @Bean
    public STOMPConnectEventListener STOMPConnectEventListener(){
        return new STOMPConnectEventListener();
    }

}
