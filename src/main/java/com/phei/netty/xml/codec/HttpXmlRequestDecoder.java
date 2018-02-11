package com.phei.netty.xml.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

import java.util.List;

/**
 * Created by guzy on 16/8/12.
 */
public class HttpXmlRequestDecoder extends XmlDecoder<FullHttpRequest> {
    @Override
    protected void decode(ChannelHandlerContext ctx, FullHttpRequest msg, List<Object> out) throws Exception {
        out.add(new HttpXmlRequest(msg,decode(ctx,msg.content())));
    }
}
