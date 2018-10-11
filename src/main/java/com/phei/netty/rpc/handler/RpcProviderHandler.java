package com.phei.netty.rpc.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by guzy on 2018-04-10.
 */
public class RpcProviderHandler extends BaseChannelHandlerAdapter {

    @Override
    public void exceptionCaughtInner(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.err.println("rpc method invoke error");
    }

    @Override
    public Object channelReadInner(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcInfo rpcInfo=(RpcInfo)msg;
        id=rpcInfo.getId();

        RpcInfo rpcResult=new RpcInfo();

        rpcResult.setResult(invoke(rpcInfo));
        rpcResult.setId(rpcInfo.getId());
        rpcResult.setSuccess(true);
        //ctx.writeAndFlush(rpcResult);
        return rpcInfo;
    }

    @Override
    Object writeInner(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        return msg;
    }

    /**
     * 调用rpcInfo中的方法
     * @param rpcInfo
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    private Object invoke(RpcInfo rpcInfo) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class invoker=Class.forName(rpcInfo.getService());

        Class[] argClasses=new Class[rpcInfo.getArgs().length];
        for(int i=0;i<rpcInfo.getArgs().length;i++){
            argClasses[i]=rpcInfo.getArgs()[i].getClass();
        }
        Method method=invoker.getMethod(rpcInfo.getMethod(),argClasses);
        return method.invoke(invoker.newInstance(), rpcInfo.getArgs());
    }
}
