package com.phei.netty.file;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Created by guzy on 18/1/29.
 */
public class FileClient {

    public void run(int port) throws InterruptedException {
        EventLoopGroup group=new NioEventLoopGroup();
        try{
            Bootstrap b=new Bootstrap();
            b.group(group)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8),
                                    new LineBasedFrameDecoder(1024),
                                    new StringDecoder(CharsetUtil.UTF_8),
                                    new FileClientHandler()
                                   );
                        }
                    });

            ChannelFuture f=b.connect(new InetSocketAddress("127.0.0.1",port)).sync()
//                    .channel().writeAndFlush("/Users/guzy/b.txt\n")
    ;



            System.out.println("client close ");

            f.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new FileClient().run(8999);
    }
}
