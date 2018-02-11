package com.phei.netty.netty.decode;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * 订阅回应处理器
 * Created by guzy on 16/8/3.
 */
public class SubReqServerHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SubscribeReq sr=(SubscribeReq) msg;
        System.out.println("receive request["+msg+"]");
        ctx.writeAndFlush(getRespById(sr.getSubReqID()));
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private SubscribeResp getRespById(int reqId){
        SubscribeResp resp=new SubscribeResp();
        resp.setSubReqID(reqId);
        resp.setDesc("desc");
        resp.setRespCode(0);
        return resp;
    }
}
