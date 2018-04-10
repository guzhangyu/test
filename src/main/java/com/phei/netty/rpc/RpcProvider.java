package com.phei.netty.rpc;

import com.phei.netty.rpc.handler.RpcInfoSerializeHandler;
import com.phei.netty.rpc.handler.RpcProviderHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Created by guzy on 2018-04-09.
 */
public class RpcProvider {

    final int port=12345;

    public void start() throws InterruptedException {
        EventLoopGroup boss=new NioEventLoopGroup(),worker=new NioEventLoopGroup();
        try{
            ChannelFuture future=new ServerBootstrap()
                    .group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new DelimiterBasedFrameDecoder(RpcConstants.LIMIT_BYTES_RPC_INFO, Unpooled.copiedBuffer(RpcConstants.CR.getBytes())))
                                    .addLast(new RpcInfoSerializeHandler())
                            .addLast(new RpcProviderHandler());
                        }
                    })
                    .bind(port).sync();

            Channel channel=future.channel();
            channel.closeFuture().sync();
        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        RpcProvider provider=new RpcProvider();
        provider.start();
    }
}
