package com.turing.im;

import com.turing.im.handler.WebSocketHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IMServer {
    //把映射关系通过一个CoCurrentHashmap保存下来
    public static final Map<String, Channel> USERS = new ConcurrentHashMap<>(1024);//KEY是昵称，value是channel

    public static void start() {
        //第2步，比较关键的一个地方，我们首先要起两个线程，一个叫做boss，一个叫做work，之前讲核心组件的时候介绍过
        NioEventLoopGroup boss = new NioEventLoopGroup();//EventLoopGroup本质上是一个线程池
        NioEventLoopGroup worker = new NioEventLoopGroup();//EventLoopGroup本质上是一个线程池

        //通过ServerBootstrap来监听一个端口
        ServerBootstrap bootstrap = new ServerBootstrap();
        //把两个线程池给他放进去
        bootstrap.group(boss, worker)
                //指定一个Channel，前面讲过SocketChannel要封装，封装成NioServerSocketChannel
                .channel(NioServerSocketChannel.class)
                //初始化Handler
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        //我们所有的事件处理都在SocketChannel上
                        //首先获取到ChannelPipeline
                        //ChannelPipeline是一种责任链的方式
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        //获取出ChannelPipeline之后首先要去增加几个Handler，比如要支持websocket的话，那第一个要增加的就是http编解码器，
                        // 因为我们的websocket本质上还是基于http协议
                        //addLast()的意思是在尾部加进去
                        //添加http编解码器
                        pipeline.addLast(new HttpServerCodec())
                                //添加对大数据量的支持
                                .addLast(new ChunkedWriteHandler())
                                //对http消息做聚合操作,会生成HttpRequest和HttpResponse，它其实就是做了一个封装
                                .addLast(new HttpObjectAggregator(1024 * 64))
                                //到了最关键的一步,对websocket做一个支持，那怎么去采用Handler呢？
                                //首先第一个，其实websocket还有一个握手的过程，
                                // 就要采用netty自带的一个协议的handler即WebSocketServerProtocolHandler
                                .addLast(new WebSocketServerProtocolHandler("/"))
                                //还需要加一个自定义，我们要接收处理消息，还有建群、连接的确认、消息的发送、私聊群聊等等，这时我们就要自定义一个消息处理
                                //自定义一个消息处理
                                .addLast(new WebSocketHandler());

                    }
                });
        //配置bootstrap去绑定一个端口，绑定完端口之后，Netty的一个Server程序就搭建完成了
        ChannelFuture future = bootstrap.bind(8080);//2023/11/15   这个future也没有使用啊，怎么绑定的8080
    }
}
