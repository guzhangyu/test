package com.phei.netty.netty.decode;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * Created by guzy on 16/8/3.
 */
public class SubReqClient {

    public void connect(String host,int port) throws InterruptedException {
        EventLoopGroup group=new NioEventLoopGroup();

        try{
            Bootstrap b=new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ch.pipeline().addLast(
                                    new ObjectDecoder(1024, ClassResolvers.cacheDisabled(this.getClass().getClassLoader()))
                            ).addLast(
                                    new ObjectEncoder()
                            ).addLast(
                                    new SubReqClientHandler()
                            );
                        }
                    });

            //发起异步连接操作
            ChannelFuture f=b.connect(host,port).sync();

            //等待客户端关闭
            f.channel().closeFuture().sync();
        }finally {
            //优雅退出，释放nio线程组
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new SubReqClient().connect("127.0.0.1",8080);
    }
}
