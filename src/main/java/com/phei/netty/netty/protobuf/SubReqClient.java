package com.phei.netty.netty.protobuf;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 * 对象client
 * Created by guzy on 16/8/3.
 */
public class SubReqClient {

    public void connect(String host,int port) throws InterruptedException {
        EventLoopGroup group=new NioEventLoopGroup();

        try{
            Bootstrap b=new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            //这是一个链路，比如channelActive链路,channelRead链路，channelWrite链路,connect链路
                            // 通过ctx.fire来实现传递,从上至下；
                            // connect不需要手动传递
                            ch.pipeline()
                                    .addLast(new ProtobufVarint32FrameDecoder())
                                    .addLast(new ProtobufDecoder(
                                            SubscribeRespProto.SubscribeResp.getDefaultInstance()
                                    ))
                                    .addLast(new ProtobufVarint32LengthFieldPrepender())
                                    .addLast(new ProtobufEncoder())
                                    .addLast(new SubReqClientHandler())
                                    .addLast(new SubReqClientHandler1());
                        }
                    });

            //发起异步连接操作
            ChannelFuture f=b.connect(host,port).sync();

            //等待客户端关闭
            f.channel().closeFuture().sync();
        }finally {
            //优雅退出，释放nio线程组
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new SubReqClient().connect("127.0.0.1",8089);
    }
}
