package com.test.netty.discard;

import com.test.netty.echo.EchoClient;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


public class DiscardClientHandler extends SimpleChannelInboundHandler<Object> {

    private ByteBuf content;
    private ChannelHandlerContext ctx;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;

        // initialize the message.
        content = ctx.alloc().directBuffer(EchoClient.SIZE).writeZero(EchoClient.SIZE);

        // send the initial message.
        generateTraffic();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        content.release();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // if server sends something, discard it
    }

    long counter;

    private void generateTraffic() {
        // flush the outbound buffer to the socket.
        // once flushed, generate the same amount of traffic again.
        ctx.writeAndFlush(content.retainedDuplicate()).addListener(trafficGenerator);
    }

    private final ChannelFutureListener trafficGenerator = new ChannelFutureListener() {
        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
            if(future.isSuccess()) {
                generateTraffic();
            }else {
                future.cause().printStackTrace();
                future.channel().close();
            }
        }
    };
}
