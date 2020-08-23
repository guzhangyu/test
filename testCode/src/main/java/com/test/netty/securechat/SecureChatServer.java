package com.test.netty.securechat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;

public class SecureChatServer {

    static final int PORT = Integer.parseInt(System.getProperty("PORT", "8007"));

    public static void main(String[] args) throws CertificateException, SSLException, InterruptedException {
        SelfSignedCertificate ssc = new SelfSignedCertificate();
        final SslContext context = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap b = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();

                            // add ssl handler first to encrypt and decrypt everything.
                            pipeline.addLast(context.newHandler(ch.alloc()));

                            // on top of the ssl handler, add the text line codec.
                            pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());

                            // add then business logic.
                            pipeline.addLast(new SecureChatServerHandler());
                        }
                    });


            ChannelFuture future = b.bind(PORT).sync();
            future.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
