package com.phei.netty.xml;

import com.phei.netty.xml.handler.HttpXmlClientHandler;
import com.phei.netty.xml.codec.HttpXmlResponseDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;


/**
 * Created by guzy on 16/8/12.
 */
public class XmlClient {
    public void connect(int port) throws InterruptedException {
        EventLoopGroup group=new NioEventLoopGroup();
        try{
            Bootstrap b=new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("http-decoder", new HttpResponseEncoder())
                                    .addLast("http-aggregator", new HttpObjectAggregator(65535))
                                    .addLast("xml-decoder", new HttpXmlResponseDecoder())

                                    .addLast("xmlClientHandler", new HttpXmlClientHandler())

                                    .addLast("http-encoder", new HttpRequestEncoder())
                                    .addLast("xml-encoder", new HttpXmlResponseDecoder())
                            ;
                        }
                    });
            //发起异步连接
            ChannelFuture f=b.connect(new InetSocketAddress(port)).sync();

            //等待客户端链路关闭
            f.channel().closeFuture().sync();
        }finally {
            //释放线程组
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new XmlClient().connect(7878);
    }
}
