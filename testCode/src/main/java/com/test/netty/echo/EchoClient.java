package com.test.netty.echo;

import com.test.netty.discard.DiscardClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import javax.net.ssl.SSLException;

public class EchoClient {

    static final boolean SSL = System.getProperty("ssl") != null;
    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8007"));
    public static final int SIZE = Integer.parseInt(System.getProperty("size", "256"));

    public static void main(String[] args) throws SSLException, InterruptedException {
        // configure ssl.git
        final SslContext sslContext;
        if(SSL) {
            sslContext = SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        }else {
            sslContext = null;
        }

        // configure the client.
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            if(sslContext != null) {
                                pipeline.addLast(sslContext.newHandler(socketChannel.alloc(), HOST, PORT));
                            }
                            pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                            pipeline.addLast(new DiscardClientHandler());
                        }
                    });

            // start the client.
            ChannelFuture future = b.connect(HOST, PORT).sync();

            // wait until the connection is closed
            future.channel().closeFuture().sync();
        }finally {
            // shutdown the event loop to terminate all threads.
            group.shutdownGracefully();
        }
    }
}
