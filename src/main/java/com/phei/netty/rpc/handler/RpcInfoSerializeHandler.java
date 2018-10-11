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
public class RpcInfoSerializeHandler extends BaseChannelHandlerAdapter {

  //  public final static ThreadLocal<String> rpcInfoId=new ThreadLocal<>();
    String id=null;

    @Override
    public Object channelReadInner(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf msgBuf=(ByteBuf)msg;

        int len=msgBuf.readInt();
        byte[] idB=new byte[len];
        msgBuf.readBytes(idB);
        id=new String(idB);

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

        byte[] bytes=msgBuf.array();
        int offset=4+idB.length;
        RpcInfo object=kryo.readObject(new Input(bytes,offset,bytes.length), RpcInfo.class);
        //id=object.getId();

//        if(object.getSuccess()==null){//说明是从consumer过来的请求(在provider部分发生)
//            rpcInfoId.set(object.getId());
//        }
        return object;
    }

    @Override
    public Object writeInner(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if(msg==null){
            System.out.println("序列化内容为空！");
            return null;
        }
        RpcInfo rpcInfo=(RpcInfo)msg;
        if(rpcInfo.getId()==null || rpcInfo.getId().trim().length()==0){
            System.out.println("序列化内容的id为空！");
            return null;
        }

        Kryo kryo=new Kryo();
        Output output=new Output(new ByteArrayOutputStream());
        kryo.writeObject(output, msg);


        byte[] idB=rpcInfo.getId().getBytes();
        byte[] bytes=output.toBytes();
        ByteBuf byteBuf=Unpooled.buffer(bytes.length+2+idB.length+4);
        byteBuf.writeInt(idB.length);
        byteBuf.writeBytes(idB);
        byteBuf.writeBytes(bytes);
        byteBuf.writeBytes(RpcConstants.CR.getBytes());

        ctx.writeAndFlush(byteBuf);

        return null;
    }

    @Override
    public void exceptionCaughtInner(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.err.println("serialize error");
    }



//    protected static void writeExceptionInfo(ChannelHandlerContext ctx, Throwable cause) {
//        cause.printStackTrace();
//        String id=rpcInfoId.get();
//        if(id!=null){
//            RpcInfo rpcResult=new RpcInfo();
//            rpcResult.setResult(cause.getMessage());
//           // rpcResult.setStackTrace(cause.getStackTrace());
//            rpcResult.setException(cause.getClass().getName());
//            rpcResult.setId(id);
//            rpcResult.setSuccess(false);
//            ctx.writeAndFlush(rpcResult);
//        }
//    }
}
