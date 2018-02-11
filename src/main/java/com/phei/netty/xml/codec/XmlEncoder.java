package com.phei.netty.xml.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;

import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by guzy on 16/8/10.
 */
public abstract class XmlEncoder<T> extends MessageToMessageEncoder<T> {

    protected ByteBuf encode(ChannelHandlerContext ctx, Object msg) throws Exception {
        IBindingFactory factory= BindingDirectory.getFactory(msg.getClass());
        if(factory==null){
            return null;
        }

        IMarshallingContext mctx=factory.createMarshallingContext();
        mctx.setIndent(2);
        StringWriter writer=new StringWriter();
        mctx.marshalDocument(msg,"utf-8",null,writer);
        writer.close();

        return Unpooled.copiedBuffer(msg.getClass().toString()+";;;"+writer.toString(), Charset.forName("UTF-8"));
    }


}
