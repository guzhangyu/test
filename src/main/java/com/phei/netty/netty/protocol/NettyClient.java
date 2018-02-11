package com.phei.netty.netty.protocol;

import com.phei.netty.netty.protocol.dataformat.NettyMessageDecoder;
import com.phei.netty.netty.protocol.dataformat.NettyMessageEncoder;
import com.phei.netty.netty.protocol.handler.HeartBeatReqHandler;
import com.phei.netty.netty.protocol.handler.LoginAuthReqHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 自定义协议解析client
 * Created by guzy on 16/8/15.
 */
public class NettyClient {

    private ScheduledExecutorService executor= Executors.newScheduledThreadPool(1);

    public void connect(final String host, final int port) throws InterruptedException {
        EventLoopGroup group=new NioEventLoopGroup();
        try{
            Bootstrap b=new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyMessageDecoder(1024 * 1024, 4, 4))
                                    .addLast("messageEncoder", new NettyMessageEncoder())
                                    .addLast("readTimeoutHandler", new ReadTimeoutHandler(50))
                                    .addLast("loginAuthHandler", new LoginAuthReqHandler())
                                    .addLast(new HeartBeatReqHandler())
                            ;
                        }
                    });
            ChannelFuture future=b.connect(new InetSocketAddress(host,port)
                   ).sync();// new InetSocketAddress(NettyConstants.LOCAL_IP,NettyConstants.LOCAL_PORT)
            future.channel().closeFuture().sync();
        }finally {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try{
                        TimeUnit.SECONDS.sleep(5);
                        try{
                            connect(host,port);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new NettyClient().connect(NettyConstants.LOCAL_IP,NettyConstants.REMOTE_PORT);
    }
}
