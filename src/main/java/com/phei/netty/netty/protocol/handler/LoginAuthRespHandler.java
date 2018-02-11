package com.phei.netty.netty.protocol.handler;

import com.phei.netty.netty.protocol.domain.Header;
import com.phei.netty.netty.protocol.domain.MessageType;
import com.phei.netty.netty.protocol.domain.NettyMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by guzy on 16/8/15.
 */
public class LoginAuthRespHandler extends ChannelHandlerAdapter {

    private Map<String,Boolean> nodeCheck =new ConcurrentHashMap<String, Boolean>();
    private String[]whiteList=new String[]{"127.0.0.1"};

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message=(NettyMessage)msg;

        if(message.getHeader()!=null
                && message.getHeader().getType()== MessageType.LOGIN_REQ.value()){//是登录请求
            String nodeIndex=ctx.channel().remoteAddress().toString();
            NettyMessage loginResp=null;

            if(nodeCheck.containsKey(nodeIndex)){//重复登录
                loginResp=buildResponse((byte)-1);
            }else{
                InetSocketAddress address=(InetSocketAddress)ctx.channel().remoteAddress();
                String ip=address.getAddress().getHostAddress();
                boolean isOk=false;
                for(String wip:whiteList){
                    if(wip.equals(ip)){
                        isOk=true;
                        break;
                    }
                }
                loginResp = buildResponse(isOk?(byte)0:(byte)-1);
                if(isOk){
                    nodeCheck.put(nodeIndex,true);
                }
            }

            System.out.println("The login response is :"+loginResp+" body ["+loginResp.getBody()+" ] ");
            ctx.writeAndFlush(loginResp);
        }else{
            ctx.fireChannelRead(msg);
        }
    }

    /**
     * 登录结果构造
     * @param b
     * @return
     */
    private NettyMessage buildResponse(byte b){
        NettyMessage message=new NettyMessage();

        Header header=new Header();
        header.setType(MessageType.LOGIN_RESP.value());
        message.setHeader(header);

        message.setBody(b);

        return message;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        nodeCheck.remove(ctx.channel().remoteAddress().toString());
        cause.printStackTrace();
        ctx.close();
        ctx.fireExceptionCaught(cause);
    }
}
