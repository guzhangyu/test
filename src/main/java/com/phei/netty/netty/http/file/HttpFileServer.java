package com.phei.netty.netty.http.file;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;


/**
 * Created by guzy on 16/8/4.
 */
public class HttpFileServer {

    private  static final String DEFAULT_URL="/com/phei/netty";

    public void run(String host, final String url,int port) throws InterruptedException {
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        EventLoopGroup workerGroup=new NioEventLoopGroup();

        try{
            ServerBootstrap b=new ServerBootstrap();
            b.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast("http-decoder",new HttpRequestDecoder())
                                    .addLast("http-aggregator",new HttpObjectAggregator(65536))
                                    .addLast("http-encoder",new HttpResponseEncoder())
                                    .addLast("http-chunked",new ChunkedWriteHandler())
                                    .addLast("fileServerHandler",new HttpFileServerHandler(url));
                        }
                    });

            ChannelFuture future=b.bind(host,port).sync();

            System.out.println("http 文件目录服务器启动,网址是:http://"+host+":"+port+url);
            future.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new HttpFileServer().run("127.0.0.1",DEFAULT_URL,8084);
    }
}
