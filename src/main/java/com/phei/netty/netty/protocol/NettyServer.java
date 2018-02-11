package com.phei.netty.netty.protocol;

import com.phei.netty.netty.protocol.dataformat.NettyMessageDecoder;
import com.phei.netty.netty.protocol.dataformat.NettyMessageEncoder;
import com.phei.netty.netty.protocol.handler.HeartBeatRespHandler;
import com.phei.netty.netty.protocol.handler.LoginAuthRespHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;


/**
 * netty 服务端，自定义协议解析
 * Created by guzy on 16/8/15.
 */
public class NettyServer {

    public void bind() throws InterruptedException {
        EventLoopGroup bossGroup=new NioEventLoopGroup(),
                workerGroup=new NioEventLoopGroup();
       // try{
            ServerBootstrap b=new ServerBootstrap();
            b.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyMessageDecoder(1024 * 1024, 4, 4))
                                    .addLast(new NettyMessageEncoder())
                                    .addLast(new ReadTimeoutHandler(50))
                                    .addLast(new LoginAuthRespHandler())
                                    .addLast(new HeartBeatRespHandler());
                        }
                    });

            b.bind(NettyConstants.LOCAL_IP,NettyConstants.REMOTE_PORT).sync();
            System.out.println(String.format("netty server start ok(%s:%s)",NettyConstants.LOCAL_IP,NettyConstants.REMOTE_PORT));
//        }
//        finally {
//            bossGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
//        }
    }

    public static void main(String[] args) throws InterruptedException {
        new NettyServer().bind();
    }
}
