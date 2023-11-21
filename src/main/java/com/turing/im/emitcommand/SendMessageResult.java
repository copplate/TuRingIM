package com.turing.im.emitcommand;

import com.alibaba.fastjson2.JSON;
import com.turing.im.command.ChatMessage;
import com.turing.im.command.ConnectCommand;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class SendMessageResult extends ChatMessage {//接收到客户端的消息后，把它发给另一个客户端
    private boolean flag;
    private String data;
    private String msg;

    public SendMessageResult(ChatMessage chatMessage, boolean flag, String data, String msg) {
        super(new ConnectCommand(chatMessage.getCode(),chatMessage.getNickName()), chatMessage.getType(), chatMessage.getTarget(), chatMessage.getContent());
        this.flag = flag;
        this.data = data;
        this.msg = msg;
    }

    public static TextWebSocketFrame success(ChatMessage chatMessage, String message) {
        return new TextWebSocketFrame(JSON.toJSONString(new SendMessageResult(chatMessage,true, message,null)));
    }
}
