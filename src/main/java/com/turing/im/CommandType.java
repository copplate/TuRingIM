package com.turing.im;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommandType {//指令的类型枚举

    //我们需要通过连接建立映射关系,建立连接的主要作用是给它放到关系表里面
    /**
     * 建立连接
     * */
    CONNECTION(10001),
    /**
     * 聊天消息
     * */
    CHAT(10002),
    ERROR(-1),

    ;
    private final Integer code;//根据code码,对应不同的类型

    //写一个match方法，根据怎么获取到我们的，去匹配一下
    public static CommandType match(Integer code) {//返回这个枚举类型
        //到这边就不去加那些缓存了，直接用一个for循环来处理
        for (CommandType value : CommandType.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return ERROR;//如果找不到匹配的枚举，就返回ERROR。
    }

}
//如果枚举比较多的话，建议写一个Hashmap存起来，查找效率更高，用for循环的查找效率是O(n)
