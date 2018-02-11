package com.phei.netty.netty.protocol.handler;

import com.phei.netty.netty.protocol.domain.Header;
import com.phei.netty.netty.protocol.domain.MessageType;
import com.phei.netty.netty.protocol.domain.NettyMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.TimeUnit;

/**
 * 心跳请求处理器
 * Created by guzy on 16/8/15.
 */
public class HeartBeatReqHandler extends ChannelHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message=(NettyMessage)msg;
        if(message.getHeader()!=null){
            if(message.getHeader().getType()== MessageType.LOGIN_RESP.value()){
                ctx.executor().scheduleAtFixedRate(new Heartbeat(ctx.channel()),1000,5000, TimeUnit.MILLISECONDS);
                return;
            }else if(message.getHeader().getType()==MessageType.HEART_BEAT_RESP.value()){
                System.out.println("get heart beat resp!!");
                return;
            }
        }
        ctx.fireChannelRead(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //super.exceptionCaught(ctx, cause);
        ctx.close();
        ctx.fireExceptionCaught(cause);
        cause.printStackTrace();
    }

    class Heartbeat implements Runnable{

        Channel channel;

        public Heartbeat(Channel channel) {
            this.channel=channel;
        }

        @Override
        public void run() {
            NettyMessage message=new NettyMessage();
            Header header=new Header();
            header.setType(MessageType.HEART_BEAT_REQ.value());
            message.setHeader(header);

            channel.writeAndFlush(message);
        }
    }


}
