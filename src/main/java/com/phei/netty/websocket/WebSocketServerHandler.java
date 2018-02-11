package com.phei.netty.websocket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.apache.log4j.Logger;

import java.net.SocketAddress;
import java.util.Date;

import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaders.setContentLength;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;

/**
 * websocket 服务 处理器
 * Created by guzy on 16/8/13.
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object>{

    private static final Logger logger= Logger.getLogger(WebSocketServerHandler.class);

    WebSocketServerHandshaker handshaker;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.name());
        super.channelRegistered(ctx);
    }

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        super.connect(ctx, remoteAddress, localAddress, promise);
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
        //http接入
        if(msg instanceof FullHttpRequest){
            handleHttpRequest(ctx,(FullHttpRequest)msg);
        }

        //websocket 接入
        if(msg instanceof WebSocketFrame){
            handleWebSocketFrame(ctx,(WebSocketFrame)msg);
        }
    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        if(!req.getDecoderResult().isSuccess()
                || !"websocket".equals(req.headers().get("Upgrade"))){
            sendHttpResponse(ctx,req,new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,BAD_REQUEST));
            return;
        }

        //构造握手响应返回，本机测试
        WebSocketServerHandshakerFactory wsFactory=new WebSocketServerHandshakerFactory("ws://localhost:8088/websocket",null,false);
        handshaker=wsFactory.newHandshaker(req);
        if(handshaker==null){
            WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
        }else{
            handshaker.handshake(ctx.channel(),req);
        }
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        //判断是否关闭链路的指令
        if(frame instanceof CloseWebSocketFrame){
            handshaker.close(ctx.channel(),(CloseWebSocketFrame)frame.retain());
            return;
        }

        //判断是否ping消息
        if(frame instanceof PingWebSocketFrame){
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }

        //本例程仅支持文本消息，不支持二进制消息
        if(! (frame instanceof TextWebSocketFrame)){
            throw new UnsupportedOperationException(String.format("%s frame types not supported",frame.getClass().getName()));
        }

        //返回应答消息
        String request =((TextWebSocketFrame)frame).text();
        if(logger.isDebugEnabled()){
            logger.debug(String.format("%s received %s",ctx.channel(),request));
        }
        ctx.channel().writeAndFlush(new TextWebSocketFrame(request+" , 欢迎使用 Netty WebSocket 服务，现在时刻:"+new Date().toString()));
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx,FullHttpRequest req,FullHttpResponse res){
        //返回应答给客户端
        if(res.getStatus().code()!=200){
            ByteBuf buf= Unpooled.copiedBuffer(res.getStatus().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
            setContentLength(res,res.content().readableBytes());
        }

        //如果非keep-alive，关闭连接
        ChannelFuture f=ctx.channel().writeAndFlush(res);
        if(!isKeepAlive(req) || res.getStatus().code()!=200){
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
