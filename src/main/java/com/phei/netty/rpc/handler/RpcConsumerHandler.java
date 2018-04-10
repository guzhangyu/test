package com.phei.netty.rpc.handler;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.util.HashMap;
import java.util.Map;

/**
 * 消费者处理器
 * Created by guzy on 2018-04-10.
 */
public class RpcConsumerHandler extends ChannelHandlerAdapter {

    Map<String,RpcInfo> rpcInfoMap=new HashMap<>();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcInfo rpcInfo=(RpcInfo)msg;
        RpcInfo targetRpcInfo=rpcInfoMap.get(rpcInfo.getId());
        targetRpcInfo.setSuccess(rpcInfo.getSuccess());
        targetRpcInfo.setException(rpcInfo.getException());
        targetRpcInfo.setResult(rpcInfo.getResult());
        synchronized (targetRpcInfo){
            targetRpcInfo.notify();
        }
        super.channelRead(ctx, msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        RpcInfo rpcInfo=(RpcInfo)msg;
        rpcInfo.setId(System.currentTimeMillis()+"");
        rpcInfoMap.put(rpcInfo.getId(),rpcInfo);
        super.write(ctx, msg, promise);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("rpcInfoMap deal error");
    }
}
