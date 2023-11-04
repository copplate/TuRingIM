package com.turing.im;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor//全参构造
@NoArgsConstructor//无参构造
@Data
public class Command {

    /**
     * 连接信息编码
     * */
    private Integer code;//状态码1、发群消息2、建立连接。根据这些指令，才能进行相对应的操作
    /**
     * 昵称
     * */
    private String nickName;
}
