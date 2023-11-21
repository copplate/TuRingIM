package com.turing.im.handler;

import com.alibaba.fastjson2.JSON;
import com.turing.im.IMServer;
import com.turing.im.Result;
import com.turing.im.command.ChatMessage;
import com.turing.im.emitcommand.ReceiveMessageResult;
import com.turing.im.emitcommand.SendMessageResult;
import com.turing.im.typeenum.MessageType;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.internal.StringUtil;

import static com.turing.im.typeenum.CommandType.SEND_HAND;
import static com.turing.im.typeenum.CommandType.SEND;

public class ReceiveChatHandler {//从客户端接收到聊天消息后如何处理

    public static void execute(ChannelHandlerContext ctx, TextWebSocketFrame frame) {

        //1、第一步，把消息的格式转成ChatMessage
        ChatMessage chat = JSON.parseObject(frame.text(), ChatMessage.class);
        System.out.println("这是从客户端接收准备发给另一个客户端的消息---frame.text()---" + frame.text());
        //接收到消息，就拿到了目标对象，然后再判断是私聊消息还是群聊消息，此处只处理私聊消息
        switch (MessageType.match(chat.getType())) {
            case PRIVATE:
                //PRIVATE情况下，第一步，确认目标对象不为空
                if (StringUtil.isNullOrEmpty(chat.getTarget())) {
                    ctx.channel().writeAndFlush(Result.fail("消息发送失败，发送消息前请指定接收对象"));
                    return;
                }
                //取出发送对象的channel，给他返回一条成功信息
//                    Channel channel2 = IMServer.USERS.get(chat.getNickName());
                ChatMessage chatMessage2 = chat;
                chatMessage2.setCode(SEND_HAND.getCode());
                ctx.channel().writeAndFlush(ReceiveMessageResult.success(chatMessage2, "消息发送成功"));
                //取出目标对象的Channel
                Channel channel = IMServer.USERS.get(chat.getTarget());
                if (channel == null || !channel.isActive()) {//isActive是看在不在线上
//                        ctx.channel().writeAndFlush(Result.fail("消息发送失败，" + chat.getTarget() + "不在线"));
                    //把消息存入离线队列
                } else {//如果在线，就直接发送消息
                    ChatMessage chatMessage = chat;
                    chatMessage.setCode(SEND.getCode());//转换一下消息类型
                    channel.writeAndFlush(SendMessageResult.success(chatMessage, "你的好友给你发送的消息"));
                    //把消息存入待确认队列
                }
                break;
            //所有的消息读写都在Channel里进行
            default:
                ctx.channel().writeAndFlush(Result.fail("不支持的消息类型"));
                break;

        }
    }
}
