package com.phei.netty.xml.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IUnmarshallingContext;

import java.io.StringReader;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by guzy on 16/8/10.
 */
public abstract class XmlDecoder<T> extends MessageToMessageDecoder<T> {

    Logger logger=Logger.getLogger("com.phei.netty.xml.handler.XmlDecoder");


    protected Object decode(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        final byte[] array;
       // final int offset;
        final int length = msg.readableBytes();
        if(length==0){
            logger.warning("decode content is empty");
            return null;
        }
        if (msg.hasArray()) {
            array = msg.array();
            //offset = msg.arrayOffset() + msg.readerIndex();
        } else {
            array = new byte[length];
            msg.getBytes(msg.readerIndex(), array, 0, length);
            //offset = 0;
        }

        String xml=new String(array);
        String []strs=xml.split(";;;");
        if(strs.length!=2){
            logger.warning(String.format("%s don't have ;;;",xml));
            return null;
        }
        Class cls=Class.forName(strs[0]);
        if(cls==null){
            logger.warning(String.format("%s is not a class",strs[0]));
            return null;
        }
        IBindingFactory factory= BindingDirectory.getFactory(cls);
        if(factory==null){
            logger.warning(String.format("%s don't have factory ",strs[0]));
            return null;
        }
        IUnmarshallingContext uctx=factory.createUnmarshallingContext();
        return uctx.unmarshalDocument(new StringReader(strs[1]));
    }
}
