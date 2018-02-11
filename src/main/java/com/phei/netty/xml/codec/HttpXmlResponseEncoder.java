package com.phei.netty.xml.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.util.List;

/**
 * 内容为xml的http 响应加密器
 * Created by guzy on 16/8/12.
 */
public class HttpXmlResponseEncoder extends XmlEncoder<HttpXmlResponse> {
    @Override
    protected void encode(ChannelHandlerContext ctx, HttpXmlResponse msg, List<Object> out) throws Exception {
        ByteBuf body=encode(ctx, msg.getResult());
        FullHttpResponse response=msg.getHttpResponse();

        if(response==null){
            response=new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,body);
        }else{
            response=new DefaultFullHttpResponse(response.getProtocolVersion(),response.getStatus(),body);
        }
        out.add(response);
    }
}
