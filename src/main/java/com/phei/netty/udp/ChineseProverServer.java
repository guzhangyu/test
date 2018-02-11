package com.phei.netty.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * 广播服务端
 * Created by guzy on 16/8/13.
 */
public class ChineseProverServer {

    public void run(int port) throws InterruptedException {
        EventLoopGroup group=new NioEventLoopGroup();
        try{
            Bootstrap b =new Bootstrap();
            b.group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST,true)
                    .handler(new ChineseProverServerHandler())
                    .bind(port).sync()
                    .channel().closeFuture().await();
        }finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new ChineseProverServer().run(8889);
    }
}
