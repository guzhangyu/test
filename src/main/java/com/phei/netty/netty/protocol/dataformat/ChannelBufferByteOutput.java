package com.phei.netty.netty.protocol.dataformat;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.ByteOutput;

import java.io.IOException;

/**
 * byteBuf实现的 ChannelBufferByteOutput
 * Created by guzy on 16/8/15.
 */
public class ChannelBufferByteOutput implements ByteOutput {

    private final ByteBuf buf;

    public ChannelBufferByteOutput(ByteBuf buf) {
        this.buf = buf;
    }

    @Override
    public void write(int i) throws IOException {
        buf.writeByte(i);
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        buf.writeBytes(bytes);
    }

    @Override
    public void write(byte[] bytes, int i, int i1) throws IOException {
        buf.writeBytes(bytes,i,i1);
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public void flush() throws IOException {

    }
}
