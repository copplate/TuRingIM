package com.turing.im.handler;

import com.alibaba.fastjson2.JSON;
import com.turing.im.command.ConnectCommand;
import com.turing.im.IMServer;
import com.turing.im.emitcommand.ConnectCommandResult;
import io.netty.channel.ChannelHandlerContext;

public class DisConnectionHandler {
    public static void execute(ChannelHandlerContext ctx, ConnectCommand connectCommand) {
        if (!IMServer.USERS.containsKey(connectCommand.getNickName())) {//如果该用户未在线，就不能给它下线
            ctx.channel().writeAndFlush(ConnectCommandResult.fail(connectCommand,"下线失败，该用户未在线"));
            ctx.channel().disconnect();//然后把他的连接断掉
            return;
        }
        IMServer.USERS.remove(connectCommand.getNickName());
        ctx.channel().disconnect();//然后把他的连接断掉
        ctx.channel().writeAndFlush(ConnectCommandResult.success(connectCommand,"下线成功~"));
        //打印当前在线用户
        System.out.println("下线成功，当前在线用户" + JSON.toJSONString(IMServer.USERS.keySet()));

    }
}
