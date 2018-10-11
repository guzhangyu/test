package com.phei.netty.rpc.handler;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.lang.reflect.InvocationTargetException;

/**
 * ChannelHandlerAdapter 基类
 */
public abstract class BaseChannelHandlerAdapter extends ChannelHandlerAdapter {

    String id;

    abstract void exceptionCaughtInner(ChannelHandlerContext ctx, Throwable cause) throws Exception;

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        this.exceptionCaughtInner(ctx,cause);
        writeExceptionInfo(ctx,cause,id);
        super.exceptionCaught(ctx, cause);
    }


    abstract Object channelReadInner(ChannelHandlerContext ctx, Object msg) throws Exception;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try{
            Object o=this.channelReadInner(ctx, msg);
            if(o!=null){
                super.channelRead(ctx,o);
            }
        }catch (Exception e){
            writeExceptionInfo(ctx,e,id);
        }
        id=null;
    }

    //传递给下一个handler的任务就交给base处理了，所以要返回对象
    abstract Object writeInner(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception;

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        try{
            Object o=this.writeInner(ctx, msg, promise);
            if(o!=null){
                super.write(ctx, msg, promise);
            }
        }catch (Exception e){
            writeExceptionInfo(ctx,e,id);
        }
        id=null;
    }

    /**
     * 出现异常时，封装称rpc异常实体，此次执行后，因为try..catch...了，就不会反复执行
     * @param ctx
     * @param cause
     * @param id
     */
    protected static void writeExceptionInfo(ChannelHandlerContext ctx, Throwable cause,String id) {
        try{
            cause.printStackTrace();
            if(id!=null){
                RpcInfo rpcResult=new RpcInfo();

                // rpcResult.setStackTrace(cause.getStackTrace());
                if(cause instanceof InvocationTargetException){
                    cause=((InvocationTargetException)cause).getTargetException();
                }
                rpcResult.setResult(cause.getMessage());
                rpcResult.setException(cause.getClass().getName());
                rpcResult.setId(id);
                rpcResult.setSuccess(false);
                ctx.writeAndFlush(rpcResult);
            }
        }catch (Exception e){
            e.printStackTrace();
            //如果在执行过程中出错了，就不重试了
        }
    }

    //    abstract void channelReadCompleteInner(ChannelHandlerContext ctx) throws Exception;
//
//    @Override
//    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        try{
//            this.channelReadCompleteInner(ctx);
//        }catch (Exception e){
//            writeExceptionInfo(ctx,e,id);
//        }
//        id=null;
//        super.channelReadComplete(ctx);
//    }
}
