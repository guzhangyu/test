package com.phei.netty.netty.protocol.dataformat;

import com.phei.netty.netty.protocol.domain.NettyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by guzy on 16/8/15.
 */
public class NettyMessageEncoder extends MessageToMessageEncoder<NettyMessage> {

    MarshallingEncoder marshallingEncoder;

    public NettyMessageEncoder() throws IOException {
        marshallingEncoder=new MarshallingEncoder();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, NettyMessage msg, List<Object> out) throws Exception {
        if(msg==null || msg.getHeader()==null){
            throw new Exception("the encode message is null");
        }
        ByteBuf sendBuf = Unpooled.buffer();
        sendBuf.writeInt(msg.getHeader().getCrcCode());
        sendBuf.writeInt(msg.getHeader().getLength());
        sendBuf.writeLong(msg.getHeader().getSessionID());
        sendBuf.writeInt(msg.getHeader().getType());
        sendBuf.writeInt(msg.getHeader().getPriority());

        if(msg.getHeader().getAttachment()==null){
            sendBuf.writeInt(0);
        }else{
            sendBuf.writeInt(msg.getHeader().getAttachment().size());

            for(Map.Entry<String,Object> item:msg.getHeader().getAttachment().entrySet()){
                byte []keyArray=item.getKey().getBytes("UTF-8");
                sendBuf.writeInt(keyArray.length);
                sendBuf.writeBytes(keyArray);

                marshallingEncoder.encode(item.getValue(),sendBuf);
            }
        }

        if(msg.getBody()!=null){
            marshallingEncoder.encode(msg.getBody(),sendBuf);
        }
        else{
            sendBuf.writeInt(0);
        }
        sendBuf.setInt(4,sendBuf.readableBytes());

        out.add(sendBuf);
    }
}
