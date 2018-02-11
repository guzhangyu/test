package com.phei.netty.udp;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


/**
 * 报文数据handler
 * Created by guzy on 16/8/13.
 */
public class ChineseProverServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private static final String[] sentences=new String[]{"只要功夫深，铁棒磨成针。","旧时王谢堂前燕，飞入寻常百姓家","无毒不丈夫"};

    private String getSentence(){
        return sentences[ThreadLocalRandom.current().nextInt(sentences.length)];
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        String req=msg.content().toString(CharsetUtil.UTF_8);
        System.out.println(req);

        if("谚语字典查询".equals(req)){
            ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer("谚语查询结果:"+getSentence(),CharsetUtil.UTF_8),msg.sender()));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        ctx.close();
    }
}
