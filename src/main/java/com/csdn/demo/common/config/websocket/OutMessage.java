package com.csdn.demo.common.config.websocket;

/**
 * 消息推送实体
 */
public class OutMessage {

    private String content;

    public OutMessage() {
    }

    public OutMessage(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

}
