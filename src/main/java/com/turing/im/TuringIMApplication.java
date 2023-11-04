package com.turing.im;

import com.alibaba.fastjson2.JSON;
import org.example.Main;
import org.junit.Test;

public class TuringIMApplication {//启动类
    public static void main(String[] args) {
        IMServer.start();//在Application里引用方法启动Server程序
    }

    @Test
    public void getString() {
        String s = JSON.toJSONString(new Command(10001, "接天莲叶无穷碧"));
        System.out.println(s);

    }
}
