package com.test.netty.securechat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SecureChatClient {

    static final int PORT = Integer.parseInt(System.getProperty("port", "8007"));

    public static void main(String[] args) throws IOException, InterruptedException {
        final SslContext sslContext = SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE).build();

        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap b = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .handler(new SecureChatClientInitializer(sslContext));



            Channel ch = b.connect("127.0.0.1" ,PORT).sync().channel();

            // read commands from the stdin.
            ChannelFuture lastWriteFuture = null;
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            for (;;) {
                String line = in.readLine();
                if(line == null) {
                    break;
                }

                // sends the received line to the server.
                lastWriteFuture = ch.writeAndFlush(line + "\r\n");

                // if user typed 'bye', wait until the server closes the connection.
                if ("bye".equalsIgnoreCase(line)) {
                    ch.closeFuture().sync();
                    break;
                }
            }

            // wait until all messages are flushed before closing the channel.
            if(lastWriteFuture != null){
                lastWriteFuture.sync();
            }
        }finally {
            group.shutdownGracefully();
        }
    }
}
