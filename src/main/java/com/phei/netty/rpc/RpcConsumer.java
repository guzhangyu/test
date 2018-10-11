package com.phei.netty.rpc;

import com.phei.netty.rpc.handler.RpcConsumerHandler;
import com.phei.netty.rpc.handler.RpcInfo;
import com.phei.netty.rpc.handler.RpcInfoSerializeHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.AttributeKey;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by guzy on 2018-04-09.
 */
public class RpcConsumer {

    final int port=12345;
    final String host="127.0.0.1";
    volatile Channel channel;

    public void start() throws InterruptedException {
        EventLoopGroup group=new NioEventLoopGroup();
        try{
            ChannelFuture future=new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new DelimiterBasedFrameDecoder(RpcConstants.LIMIT_BYTES_RPC_INFO, Unpooled.copiedBuffer(RpcConstants.CR.getBytes())))
                                    .addLast(new RpcInfoSerializeHandler())
                            .addLast(new RpcConsumerHandler());
                        }
                    })
                    .connect(host, port).sync();

            channel=future.channel();
            channel.closeFuture().sync();
        }finally {
            group.shutdownGracefully();
        }
    }

    public Object invoke(RpcInfo rpcInfo) throws Exception {
        while(channel==null){
            Thread.sleep(1000l);
        }
        channel.writeAndFlush(rpcInfo);

        synchronized (rpcInfo){
            try {
                rpcInfo.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(rpcInfo.getSuccess()){
            return rpcInfo.getResult();
        }
        try {
            Exception e=(Exception)Class.forName(rpcInfo.getException()).getConstructor(String.class).newInstance(rpcInfo.getResult());
            throw e;
        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (InvocationTargetException e1) {
            e1.printStackTrace();
        } catch (NoSuchMethodException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
        return new Exception("错误信息实例化失败！");
    }

    public static void main(String[] args) throws Exception {
        final RpcConsumer consumer=new RpcConsumer();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    consumer.start();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        RpcInfo rpcInfo=new RpcInfo();
        rpcInfo.setService("com.phei.netty.rpc.test.A");
        rpcInfo.setMethod("B");
        rpcInfo.setArgs(new Object[]{"C"});
        consumer.invoke(rpcInfo);
        System.out.println(rpcInfo);
    }
}
