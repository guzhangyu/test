package com.phei.netty.netty.protocol.dataformat;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.ByteInput;

import java.io.IOException;

/**
 *  通过byteBuf封装的channelBufferByteInput
 * Created by guzy on 16/8/15.
 */
public class ChannelBufferByteInput implements ByteInput {

    private final ByteBuf buf;

    public ChannelBufferByteInput(ByteBuf buf) {
        this.buf = buf;
    }

    @Override
    public int read() throws IOException {
        if(buf.isReadable()){
            return buf.readByte() & 0xff;
        }
        return -1;
    }

    @Override
    public int read(byte[] bytes) throws IOException {
        return read(bytes,0,bytes.length);
    }

    @Override
    public int read(byte[] bytes, int dstIndex, int len) throws IOException {
        int avail=available();
        if(avail<=0){
            return -1;
        }
        if(len>avail){
            len=avail;
        }
        buf.readBytes(bytes,dstIndex,len);
        return len;
    }

    @Override
    public int available() throws IOException {
        return buf.readableBytes();
    }

    @Override
    public long skip(long l) throws IOException {
        int avail=available();
        if(avail<l){
            l=avail;
        }
        buf.readerIndex((int)(buf.readerIndex()+l));
        return l;
    }

    @Override
    public void close() throws IOException {

    }
}
