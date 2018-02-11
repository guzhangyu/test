package com.phei.netty.netty.decode;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by guzy on 16/8/3.
 */
public class SubReqClientHandler extends ChannelHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for(int i=0;i<10;i++){
            ctx.write(getSubReq(i));
        }
        ctx.flush();
    }

    private SubscribeReq getSubReq(int i){
        SubscribeReq req=new SubscribeReq();
        req.setSubReqID(i);
        req.setUserName("Liliang");
        req.setAddress("current address");
        req.setPhoneNumber("1865704999");
        req.setProductName("my test product");
        return req;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("get msg:"+msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
      //  super.channelReadComplete(ctx);
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        ctx.close();
    }
}
