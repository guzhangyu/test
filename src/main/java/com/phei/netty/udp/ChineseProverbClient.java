package com.phei.netty.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;


/**
 * 广播
 * Created by guzy on 16/8/13.
 */
public class ChineseProverbClient {

    /**
     *
     * @param port 要请求的端口号
     * @throws InterruptedException
     */
    public void run(int port) throws InterruptedException {
        EventLoopGroup group=new NioEventLoopGroup();
        try{
            Bootstrap b=new Bootstrap();
            b.group(group).channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST,true)
                .handler(new ChineseProverClientHandler());
            Channel ch= b.bind(0).sync().channel();

            //向网段内的所有机器广播udp消息
            ch.writeAndFlush(new DatagramPacket(
                    Unpooled.copiedBuffer("谚语字典查询", CharsetUtil.UTF_8),
                    new InetSocketAddress("255.255.255.255",port)
            )).sync();

            if(!ch.closeFuture().await(15000)){
                System.out.println("查询超时！");
            }
        }finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new ChineseProverbClient().run(8889);
    }
}
