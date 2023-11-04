package com.turing.im.command;

import com.turing.im.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor//全参构造
@NoArgsConstructor//无参构造
@Data
public class ChatMessage extends Command {

    /**
     * 消息类型
     * */
    private Integer type;//有可能是私聊消息，也有可能是群聊消息

    /**
     * 目标接收对象
     * */
    private String target;//接收人是谁，把消息发给谁

    /**
     * 内容
     * */
    private String content;
}
