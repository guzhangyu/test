package com.phei.netty.netty.protocol.handler;

import com.phei.netty.netty.protocol.domain.Header;
import com.phei.netty.netty.protocol.domain.MessageType;
import com.phei.netty.netty.protocol.domain.NettyMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * 心跳响应处理器
 * Created by guzy on 16/8/15.
 */
public class HeartBeatRespHandler extends ChannelHandlerAdapter {

//    @Override
//    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//
//    }

    private static int count=0;


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message=(NettyMessage)msg;
        Header header=message.getHeader();
        if(header!=null && header.getType()== MessageType.HEART_BEAT_REQ.value()){
            count=0;
            NettyMessage m=new NettyMessage();
            Header h=new Header();
            h.setType(  MessageType.HEART_BEAT_RESP.value());
            m.setHeader(h);
            ctx.writeAndFlush(m);
            return;
        }
        ctx.fireChannelRead(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //super.exceptionCaught(ctx, cause);
        ctx.fireExceptionCaught(cause);
        ctx.close();
        cause.printStackTrace();
    }
}
