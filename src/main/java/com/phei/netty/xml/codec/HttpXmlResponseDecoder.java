package com.phei.netty.xml.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;

import java.util.List;

/**
 * 内容为xml的响应结果解析器
 * Created by guzy on 16/8/12.
 */
public class HttpXmlResponseDecoder extends XmlDecoder<DefaultFullHttpResponse> {

    @Override
    protected void decode(ChannelHandlerContext ctx, DefaultFullHttpResponse msg, List<Object> out) throws Exception {
        out.add(new HttpXmlResponse(msg,decode(ctx,msg.content())));
    }
}
