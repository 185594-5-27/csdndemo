package com.csdn.demo.common.config.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectEvent;

/**
 * Created by baiguantao on 2017/8/4.
 * STOMP监听类
 * 用于session注册 以及key值获取
 */
public class STOMPConnectEventListener  implements ApplicationListener<SessionConnectEvent> {

    @Autowired
    SocketSessionRegistry webAgentSessionRegistry;

    @Override
    public void onApplicationEvent(SessionConnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        //login get from browser
        String agentId = sha.getNativeHeader("login").get(0);
        String sessionId = sha.getSessionId();
        System.out.println("接收的sessionId是："+sessionId);
        webAgentSessionRegistry.registerSessionId(agentId,sessionId);
    }

}
