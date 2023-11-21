package com.turing.im.handler;

import com.alibaba.fastjson2.JSON;
import com.turing.im.command.ConnectCommand;
import com.turing.im.IMServer;
import com.turing.im.Result;
import com.turing.im.emitcommand.ConnectCommandResult;
import io.netty.channel.ChannelHandlerContext;

public class ConnectionHandler {
    //要在这能够读到channel里的数据是什么,所以增加了ChannelHandlerContext ctx参数
    public static void execute(ChannelHandlerContext ctx, ConnectCommand connectCommand) {
        if (IMServer.USERS.containsKey(connectCommand.getNickName())) {//如果该用户已存在，就不能重复上线
//            ctx.channel().writeAndFlush(Result.fail("该用户已上线，请更换昵称后再试"));
            ctx.channel().writeAndFlush(ConnectCommandResult.fail(connectCommand,"该用户已上线，请更换昵称后再试"));
            ctx.channel().disconnect();//然后把他的连接断掉
            return;
        }
        IMServer.USERS.put(connectCommand.getNickName(), ctx.channel());//这样就给它加到映射表里了
        //告诉前端，恭喜你加入成功
        ctx.channel().writeAndFlush(ConnectCommandResult.success(connectCommand,"与服务端连接建立成功"));
        //返回当前在线用户
//        ctx.channel().writeAndFlush(Result.success(JSON.toJSONString(IMServer.USERS.keySet())));
        //打印当前在线用户
        System.out.println("登录成功，当前在线用户" + JSON.toJSONString(IMServer.USERS.keySet()));

    }
}
