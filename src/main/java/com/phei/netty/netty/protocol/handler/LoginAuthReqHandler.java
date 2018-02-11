package com.phei.netty.netty.protocol.handler;

import com.phei.netty.netty.protocol.domain.Header;
import com.phei.netty.netty.protocol.domain.MessageType;
import com.phei.netty.netty.protocol.domain.NettyMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * 登录请求处理器
 * Created by guzy on 16/8/15.
 */
public class LoginAuthReqHandler extends ChannelHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(buildLoginReq());
        ctx.fireChannelActive();
    }

    private NettyMessage buildLoginReq(){
        NettyMessage message=new NettyMessage();
        Header header=new Header();
        header.setType(MessageType.LOGIN_REQ.value());
        message.setHeader(header);
        return message;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message=(NettyMessage)msg;

        if(message.getHeader()!=null && message.getHeader().getType()==MessageType.LOGIN_RESP.value()){
            byte loginResult = (Byte)message.getBody();
            if(loginResult!=(byte)0){
                ctx.close();
            }else{
                System.out.println("login is ok: "+message);
                ctx.fireChannelRead(msg);
            }
        }else{
            ctx.fireChannelRead(msg);
        }
    }
}
