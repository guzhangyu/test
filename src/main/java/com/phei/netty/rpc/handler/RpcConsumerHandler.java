package com.phei.netty.rpc.handler;

import com.sun.xml.internal.rngom.parse.host.Base;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.util.HashMap;
import java.util.Map;

/**
 * 消费者处理器
 * Created by guzy on 2018-04-10.
 */
public class RpcConsumerHandler extends BaseChannelHandlerAdapter {

    Map<String,RpcInfo> rpcInfoMap=new HashMap<>();

    @Override
    public Object channelReadInner(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcInfo rpcInfo=(RpcInfo)msg;
        RpcInfo targetRpcInfo=rpcInfoMap.get(rpcInfo.getId());
        targetRpcInfo.setSuccess(rpcInfo.getSuccess());
        targetRpcInfo.setException(rpcInfo.getException());
        targetRpcInfo.setResult(rpcInfo.getResult());
        synchronized (targetRpcInfo){
            targetRpcInfo.notify();
        }
        return targetRpcInfo;
    }

    @Override
    public Object writeInner(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        RpcInfo rpcInfo=(RpcInfo)msg;
        rpcInfo.setId(System.currentTimeMillis()+"");
        rpcInfoMap.put(rpcInfo.getId(),rpcInfo);

        id=rpcInfo.getId();
        return rpcInfo;
    }

    @Override
    public void exceptionCaughtInner(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("rpcInfoMap deal error");
    }
}
