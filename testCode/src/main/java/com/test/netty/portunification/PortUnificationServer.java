//package com.test.netty.portunification;
//
//import io.netty.bootstrap.ServerBootstrap;
//import io.netty.channel.ChannelInitializer;
//import io.netty.channel.ChannelOption;
//import io.netty.channel.EventLoopGroup;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.channel.socket.nio.NioServerSocketChannel;
//import io.netty.handler.logging.LogLevel;
//import io.netty.handler.logging.LoggingHandler;
//import io.netty.handler.ssl.SslContext;
//import io.netty.handler.ssl.SslContextBuilder;
//import io.netty.handler.ssl.util.SelfSignedCertificate;
//
//import javax.net.ssl.SSLException;
//import java.security.cert.CertificateException;
//
//public class PortUnificationServer {
//
//    static final int PORT = Integer.parseInt(System.getProperty("PORT", "1234"));
//
//    public static void main(String[] args) throws CertificateException, SSLException, InterruptedException {
//        SelfSignedCertificate certificate = new SelfSignedCertificate();
//        SslContext context = SslContextBuilder.forServer(certificate.certificate(), certificate.privateKey()).build();
//
//        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
//        EventLoopGroup workerGroup = new NioEventLoopGroup();
//        try{
//            ServerBootstrap bootstrap = new ServerBootstrap()
//                    .group(bossGroup, workerGroup)
//                    .channel(NioServerSocketChannel.class)
//                    .option(ChannelOption.SO_BACKLOG, 100)
//                    .handler(new LoggingHandler(LogLevel.INFO))
//                    .childHandler(new ChannelInitializer<SocketChannel>() {
//                        @Override
//                        protected void initChannel(SocketChannel ch) throws Exception {
//                            ch.pipeline().addLast(new PortUnificationServerHandler(context));
//                        }
//                    });
//
//
//            bootstrap.bind(PORT).sync().channel().closeFuture().sync();
//
//        }finally {
//            bossGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
//        }
//    }
//}
