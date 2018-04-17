package com.phei.netty.xml;

import com.phei.netty.xml.codec.HttpXmlRequestDecoder;
import com.phei.netty.xml.codec.HttpXmlResponseEncoder;
import com.phei.netty.xml.handler.HttpXmlServerHandler1;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

/**
 * Created by guzy on 16/8/12.
 */
public class HttpXmlServer {

    private static String CR=System.getProperty("line.separator");

    public void bind(int port) throws InterruptedException {
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        EventLoopGroup workerGroup=new NioEventLoopGroup();

        try{
            ServerBootstrap b=new ServerBootstrap();
            b.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
//                                    .addLast(new DelimiterBasedFrameDecoder(1024, Unpooled.copiedBuffer("\n".getBytes())))
//                            //.addLast(new StringDecoder())
//                            .addLast(new ChannelHandlerAdapter(){
//                                @Override
//                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//                                    System.out.println(msg);
//                                    super.channelRead(ctx, msg);
//                                }
//                            })
                                    .addLast("http-decoder", new HttpRequestDecoder())
                                    .addLast("object-aggregator", new HttpObjectAggregator(65535))
                                    .addLast("xml-decoder", new HttpXmlRequestDecoder())

                                    .addLast("http-encoder", new HttpResponseEncoder())
                                    .addLast("xml-encoder", new HttpXmlResponseEncoder())

                                    .addLast("xmlServerHandler", new HttpXmlServerHandler1())
                            ;
                        }
                    });

            ChannelFuture future=b.bind(new InetSocketAddress(port)).sync();

            System.out.println("http 订阅服务启动，网址是:http://localhost:"+port);

            future.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new HttpXmlServer().bind(7878);
    }
}
