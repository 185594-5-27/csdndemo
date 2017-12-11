package com.csdn.demo.sys.controller;

import com.csdn.demo.common.config.websocket.OutMessage;
import com.csdn.demo.common.config.websocket.SocketSessionRegistry;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
* 类描述：
* @auther linzf
* @create 2017/12/11 0011 
*/
@Controller
@RequestMapping("/websocket")
public class WebsocketController {

    /**session操作类*/
    @Autowired
    private SocketSessionRegistry webAgentSessionRegistry;
    /**消息发送工具*/
    @Autowired
    private SimpMessagingTemplate template;

    /**
     * 功能描述：给全局推送消息
     */
    @RequestMapping(value = "/sendAll",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void sendAll(){
        Map<String,Set<String>> all = webAgentSessionRegistry.getAllSessionIds();
        JSONObject jobj = new JSONObject();
        jobj.put("test","test");
        all.forEach((k,v)->{
            v.forEach(x->{
                template.convertAndSendToUser(x,"/topic/greetings",new OutMessage(jobj.toString()),createHeaders(x));
            });
        });
    }

    /**
     * 功能描述：给指定账号推送消息
     */
    @RequestMapping(value = "/sendUser",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void senduUser(){
        // 此处账号已经被写死为hyll 大家可以根据自己的实际业务来修改此处的代码
        Set<String> keys = webAgentSessionRegistry.getSessionIds("hyll");
        JSONObject jobj = new JSONObject();
        jobj.put("hyll","hyll");
        keys.forEach(x->{
            template.convertAndSendToUser(x,"/topic/greetings",new OutMessage(jobj.toString()),createHeaders(x));
        });
    }


    /**
     * 功能描述：组装JSON数据的头部数据
     * @param sessionId
     * @return
     */
    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }

}
