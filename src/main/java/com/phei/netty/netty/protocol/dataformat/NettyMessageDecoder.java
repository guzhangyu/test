package com.phei.netty.netty.protocol.dataformat;

import com.phei.netty.netty.protocol.domain.Header;
import com.phei.netty.netty.protocol.domain.NettyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//Netty的LengthFieldBasedFrameDecoder解码器，它支持自动的TCP粘包和半包处理，
//只需要给出标识消息长度的字段偏移量和消息长度自身所占的字节数，Netty就能自动实现对半包的处理。
public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {

    MarshallingDecoder marshallingDecoder;

    Logger logger=Logger.getLogger(NettyMessageDecoder.class);

    public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) throws IOException {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
        marshallingDecoder=new MarshallingDecoder();
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        //对于业务解码器来说，调用父类LengthFieldBasedFrameDecoder的解码方法后，返回的就是整包消息或者为空，
        //如果为空说明是个半包消息，直接返回继续由I/O线程读取后续的码流
        ByteBuf frame=(ByteBuf)super.decode(ctx,in);
        if(frame==null){
            //logger.error("frame is null");
            //return null;
        }
        in.resetReaderIndex();
       // in.readerIndex(0);
        if(in.readableBytes()<=0){
            return null;
        }
        NettyMessage nettyMessage=new NettyMessage();
        Header header=new Header();
        header.setCrcCode(in.readInt());
        header.setLength(in.readInt());
        header.setSessionID(in.readLong());
        header.setType(in.readInt());
        header.setPriority(in.readInt());
        nettyMessage.setHeader(header);

        int size=in.readInt();
        Map<String,Object>  att=new HashMap<String, Object>(size);
        header.setAttachment(att);
        if(size>0){
            for(int i=0;i<size;i++){
                byte[] byteArr=new byte[in.readInt()];
                in.readBytes(byteArr);
                att.put(new String(byteArr,"UTF-8"),marshallingDecoder.decode(in));
            }
        }

        if(in.readableBytes()>4){
            nettyMessage.setBody(marshallingDecoder.decode(in));
        }

        in.skipBytes(in.readableBytes());
        return nettyMessage;
    }
}
