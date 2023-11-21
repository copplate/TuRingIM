package com.turing.im.emitcommand;

import com.alibaba.fastjson2.JSON;
import com.turing.im.command.ConnectCommand;
import com.turing.im.typeenum.CommandType;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnectCommandResult extends ConnectCommand {
    private boolean flag;
    private String data;
    private String msg;

    public ConnectCommandResult(ConnectCommand connectCommand, boolean flag, String data, String msg) {
        super(connectCommand.getCode(), connectCommand.getNickName());
        this.flag = flag;
        this.data = data;
        this.msg = msg;
    }

    public static TextWebSocketFrame fail(ConnectCommand connectCommand,String message) {
        return new TextWebSocketFrame(JSON.toJSONString(new ConnectCommandResult(connectCommand,false, message,null)));
    }

    public static TextWebSocketFrame success(ConnectCommand connectCommand,String message) {
        return new TextWebSocketFrame(JSON.toJSONString(new ConnectCommandResult(connectCommand,true, message,null)));
    }
}
