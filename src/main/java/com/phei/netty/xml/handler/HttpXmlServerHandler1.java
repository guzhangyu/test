package com.phei.netty.xml.handler;

import com.phei.netty.xml.codec.HttpXmlRequest;
import com.phei.netty.xml.codec.HttpXmlResponse;
import com.phei.netty.xml.pojo.Customer;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by guzy on 16/8/12.
 */
public class HttpXmlServerHandler1 extends SimpleChannelInboundHandler<HttpXmlRequest> {
    @Override
    protected void messageReceived(final ChannelHandlerContext ctx, HttpXmlRequest msg) throws Exception {
        HttpRequest request=msg.getRequest();
        Customer customer=(Customer)msg.getBody();
        System.out.println("Http Server receive request:"+customer);

        ChannelFuture future=ctx.writeAndFlush(new HttpXmlResponse(null,customer));

        if(!isKeepAlive(request)){
            future.addListener(new GenericFutureListener<Future<? super java.lang.Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    ctx.close();
                }
            });
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if(ctx.channel().isActive()){
            sendError(ctx,INTERNAL_SERVER_ERROR);
        }
    }

    private static void sendError(ChannelHandlerContext ctx,HttpResponseStatus status){
        FullHttpResponse response=new DefaultFullHttpResponse(HTTP_1_1,status,
                Unpooled.copiedBuffer("失败："+status.toString()+"\r\n", CharsetUtil.UTF_8));
        response.headers().set(CONTENT_TYPE,"text/plain;charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

//    private boolean isKeepAlive(HttpRequest request) {
//        return request.headers().get("keep-alive")!=null;
//    }
}
