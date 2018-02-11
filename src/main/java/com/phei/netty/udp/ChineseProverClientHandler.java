package com.phei.netty.udp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;


/**
 * Created by guzy on 16/8/13.
 */
public class ChineseProverClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        String response=msg.content().toString(CharsetUtil.UTF_8);
        System.out.println(response);

        if(response.startsWith("谚语查询结果")){
            ctx.close();
        }
    }

}
