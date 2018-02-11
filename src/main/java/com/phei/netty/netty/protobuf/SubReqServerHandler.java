package com.phei.netty.netty.protobuf;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.net.SocketAddress;

/**
 * Created by guzy on 16/8/3.
 */
public class SubReqServerHandler extends ChannelHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel active");
        super.channelActive(ctx);
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SubscribeReqProto.SubscribeReq req=(SubscribeReqProto.SubscribeReq)msg;
        System.out.println("server get:"+req);
        ctx.writeAndFlush(resp(req.getSubReqID()));
    }

    private SubscribeRespProto.SubscribeResp resp(int id){
        SubscribeRespProto.SubscribeResp.Builder builder=SubscribeRespProto.SubscribeResp.newBuilder();
        builder.setSubReqID(id);
        builder.setRespCode(0);
        builder.setDesc("desc");
        return builder.build();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        ctx.close();
    }
}
