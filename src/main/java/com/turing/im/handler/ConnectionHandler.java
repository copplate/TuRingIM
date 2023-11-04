package com.turing.im.handler;

import com.alibaba.fastjson2.JSON;
import com.turing.im.Command;
import com.turing.im.IMServer;
import com.turing.im.Result;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class ConnectionHandler {
    //要在这能够读到channel里的数据是什么,所以增加了ChannelHandlerContext ctx参数
    public static void execute(ChannelHandlerContext ctx, Command command) {
        if (IMServer.USERS.containsKey(command.getNickName())) {//如果该用户已存在，就不能重复上线
            ctx.channel().writeAndFlush(Result.fail("该用户已上线，请更换昵称后再试"));
            ctx.channel().disconnect();//然后把他的连接断掉
            return;
        }
        IMServer.USERS.put(command.getNickName(), ctx.channel());//这样就给它加到映射表里了
        //告诉前端，恭喜你加入成功
        ctx.channel().writeAndFlush(Result.success("与服务端连接建立成功"));
        //返回当前在线用户
        ctx.channel().writeAndFlush(Result.success(JSON.toJSONString(IMServer.USERS.keySet())));

    }
}
