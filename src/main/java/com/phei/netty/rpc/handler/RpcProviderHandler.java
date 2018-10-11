package com.phei.netty.rpc.handler;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by guzy on 2018-04-10.
 */
public class RpcProviderHandler extends ChannelHandlerAdapter {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.err.println("rpc method invoke error");
        RpcInfoSerializeHandler.writeExceptionInfo(ctx,cause,null);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String id=null;
        try{
            RpcInfo rpcInfo=(RpcInfo)msg;
            id=rpcInfo.getId();

            RpcInfo rpcResult=new RpcInfo();
            try{
                Class invoker=Class.forName(rpcInfo.getService());

                Class[] argClasses=new Class[rpcInfo.getArgs().length];
                for(int i=0;i<rpcInfo.getArgs().length;i++){
                    argClasses[i]=rpcInfo.getArgs()[i].getClass();
                }
                Method method=invoker.getMethod(rpcInfo.getMethod(),argClasses);
                Object result=method.invoke(invoker.newInstance(), rpcInfo.getArgs());
                rpcResult.setResult(result);
            }catch (InvocationTargetException e){
                this.exceptionCaught(ctx,e.getTargetException());
                return;
            }
            rpcResult.setId(rpcInfo.getId());
            rpcResult.setSuccess(true);
            ctx.writeAndFlush(rpcResult);
            super.channelRead(ctx, msg);
        }catch (Exception e){
            RpcInfoSerializeHandler.writeExceptionInfo(ctx,e,id);
        }
    }
}
