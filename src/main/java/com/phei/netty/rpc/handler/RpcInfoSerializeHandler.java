package com.phei.netty.rpc.handler;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.phei.netty.rpc.RpcConstants;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.io.ByteArrayOutputStream;

/**
 * Created by guzy on 2018-04-10.
 */
public class RpcInfoSerializeHandler extends ChannelHandlerAdapter {

    public final static ThreadLocal<String> rpcInfoId=new ThreadLocal<>();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Kryo kryo=new Kryo();
//        int id=32323223;
//        Registration registration=new Registration(StackTraceElement.class,new FieldSerializer<StackTraceElement>(kryo,StackTraceElement.class),id);
//        registration.setInstantiator(new ObjectInstantiator(){
//
//            @Override
//            public Object newInstance() {
//                return null;
//            }
//        });
//        kryo.register();

        //kryo.register(List.class,new DefaultSerializers.CollectionsSingletonListSerializer());
        ByteBuf msgBuf=(ByteBuf)msg;
        byte[] bytes=msgBuf.array();
        RpcInfo object=kryo.readObject(new Input(bytes), RpcInfo.class);

        if(object.getSuccess()==null){//说明是从consumer过来的请求
            rpcInfoId.set(object.getId());
        }
        super.channelRead(ctx, object);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        Kryo kryo=new Kryo();
        Output output=new Output(new ByteArrayOutputStream());
        kryo.writeObject(output, msg);

        byte[] bytes=output.toBytes();
        ByteBuf byteBuf=Unpooled.buffer(bytes.length+2);
        byteBuf.writeBytes(bytes);
        byteBuf.writeBytes(RpcConstants.CR.getBytes());

        ctx.writeAndFlush(byteBuf);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.err.println("serialize error");
        writeExceptionInfo(ctx, cause);
    }

    protected static void writeExceptionInfo(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        String id=rpcInfoId.get();
        if(id!=null){
            RpcInfo rpcResult=new RpcInfo();
            rpcResult.setResult(cause.getMessage());
           // rpcResult.setStackTrace(cause.getStackTrace());
            rpcResult.setException(cause.getClass().getName());
            rpcResult.setId(id);
            rpcResult.setSuccess(false);
            ctx.writeAndFlush(rpcResult);
        }
    }
}
