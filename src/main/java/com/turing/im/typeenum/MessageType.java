package com.turing.im.typeenum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageType {//私聊消息和群聊消息
    /**
     * 私聊
     */
    PRIVATE(1),
    /**
     * 群聊
     */
    GROUP(2),
    /**
     * 从客户端接收消息
     * */
    RECEIVE(3),
    /**
     * 向客户端发送消息
     * */
    SEND(4),
    /**
     * 不支持的类型
     */
    ERROR(-1);
    private Integer type;

    public static MessageType match(Integer type) {
        for (MessageType value :
                MessageType.values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        //如果一个也没匹配上就返回ERROR
        return ERROR;
    }
//如果枚举比较多的话，建议写一个Hashmap存起来，查找效率更高，用for循环的查找效率是O(n)
}
