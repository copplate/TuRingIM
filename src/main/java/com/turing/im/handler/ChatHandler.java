package com.turing.im.handler;

import com.alibaba.fastjson2.JSON;
import com.turing.im.Command;
import com.turing.im.IMServer;
import com.turing.im.MessageType;
import com.turing.im.Result;
import com.turing.im.command.ChatMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.internal.StringUtil;

public class ChatHandler {

    public static void execute(ChannelHandlerContext ctx, TextWebSocketFrame frame) {
        try {
            //1、第一步，把消息的格式转成ChatMessage
            ChatMessage chat = JSON.parseObject(frame.text(), ChatMessage.class);
            //接收到消息，就拿到了目标对象，然后再判断是私聊消息还是群聊消息，此处只处理私聊消息
            switch (MessageType.match(chat.getType())) {
                case PRIVATE:
                    //PRIVATE情况下，第一步，确认目标对象不为空
                    if (StringUtil.isNullOrEmpty(chat.getTarget())) {
                        ctx.channel().writeAndFlush(Result.fail("消息发送失败，发送消息前请指定接收对象"));
                        return;
                    }
                    //取出目标对象的Channel
                    Channel channel = IMServer.USERS.get(chat.getTarget());
                    if (channel == null || !channel.isActive()) {//isActive是看在不在线上
                        ctx.channel().writeAndFlush(Result.fail("消息发送失败，" + chat.getTarget() + "不在线"));
                    } else {//如果在线，就直接发送消息
                        channel.writeAndFlush(Result.success("私聊消息:(" + chat.getNickName() + ")", chat.getContent()));
                    }

                    break;
                //所有的消息读写都在Channel里进行
                default: ctx.channel().writeAndFlush(Result.fail("不支持的消息类型"));
                break;

            }


        } catch (Exception e) {
            ctx.channel().writeAndFlush(Result.fail("发送消息格式错误，请确认后再试"));
        }


    }
}
