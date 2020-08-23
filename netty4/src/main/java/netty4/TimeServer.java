package netty4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class TimeServer {

    public void bind(int port){
        //配置服务端的IO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new DelimiterBasedFrameDecoder(1024, Unpooled.copiedBuffer("\n".getBytes())))
                                    .addLast(new StringDecoder())
                                    .addLast(new ChannelInboundHandlerAdapter(){
                                        @Override
                                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                            System.out.println(msg);

                                            byte[] result = ("server:" + msg + System.getProperty("line.separator")).getBytes();
                                            ByteBuf buf = Unpooled.copiedBuffer(result);
                                            ctx.writeAndFlush(buf);
                                        }
                                    });
                        }
                    });

            //绑定端口，同步等待成功
            ChannelFuture f = b.bind(port).sync();

            System.out.println(String.format("listen at port:%d", port));

            //等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new TimeServer().bind(8086);
    }
}
