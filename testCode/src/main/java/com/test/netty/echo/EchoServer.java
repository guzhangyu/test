package com.test.netty.echo;

import com.test.netty.discard.DiscardServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;

public class EchoServer {

    static final boolean SSL = System.getProperty("SSL") != null;
    static final int PORT = Integer.parseInt(System.getProperty("PORT", "8007"));

    public static void main(String[] args) throws CertificateException, SSLException, InterruptedException {
        final SslContext context ;
        if(SSL) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            context = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        }else {
            context = null;
        }

        // configure the server.
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        final ChannelInboundHandlerAdapter serverHandler = new DiscardServerHandler();
        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            if(context != null) {
                                p.addLast(context.newHandler(ch.alloc()));
                            }
                            p.addLast(serverHandler);
                        }
                    });

            // start the server.
            ChannelFuture f = b.bind(PORT).sync();

            // wait until the server socket is closed.
            f.channel().closeFuture().sync();
        }finally {
            // shutdown all event loops to terminate all threads
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

