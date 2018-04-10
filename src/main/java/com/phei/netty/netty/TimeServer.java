package com.phei.netty.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;


/**
 * Created by guzy on 16/8/1.
 */
public class TimeServer {

    public static void main(String[] args) throws Exception {
        new TimeServer().bind(8086);
    }

    public void bind(int port) throws Exception {
        //配置服务端的nio线程组
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

      try{
          ServerBootstrap b=new ServerBootstrap();
          b.group(bossGroup,workerGroup)
                  .channel(NioServerSocketChannel.class)
                  .option(ChannelOption.SO_BACKLOG,1024)
                  .childHandler(new ChildChannelHandler());

          //绑定端口，同步等待成功
          ChannelFuture f=b.bind(port).sync();

          System.out.println(String.format("listen at %d",port));

          //等待服务端监听端口关闭
          f.channel().closeFuture().sync();
      }finally {
          {
              //释放线程池资源
              bossGroup.shutdownGracefully();
              workerGroup.shutdownGracefully();
          }
      }
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel>{

        @Override
        protected void initChannel(SocketChannel channel) throws Exception {
           // channel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, Unpooled.copiedBuffer("\n".getBytes())));
           // channel.pipeline().addLast(new FixedLengthFrameDecoder(17));
            channel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, Unpooled.copiedBuffer("\n".getBytes())));
            channel.pipeline().addLast(new StringDecoder());
           // channel.pipeline().addLast(new com.phei.netty.bio.simple.TimeServerHandler());
            channel.pipeline().addLast(new ChannelHandlerAdapter(){
                @Override
                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                    System.out.println(msg);
                    byte[] result=("server:"+msg+System.getProperty("line.separator")).getBytes();
                    ByteBuf buf= Unpooled.copiedBuffer(result);
                    ctx.writeAndFlush(buf);
                }

                @Override
                public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
                    ctx.flush();
                }

                @Override
                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                    cause.printStackTrace();
                    ctx.close();
                }
            });
        }
    }
}
