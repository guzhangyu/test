package netty4;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TimeClient {

    private static String CR = System.getProperty("line.separator");

    public void connect(String host, int port){
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new DelimiterBasedFrameDecoder(1024, Unpooled.copiedBuffer(CR.getBytes())))
                                    .addLast(new StringDecoder())
                                    .addLast(new ChannelInboundHandlerAdapter(){
                                        @Override
                                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                                            String line = br.readLine();
                                            while(!"EOF".equals(line)){
                                                byte[] bytes = (line + CR).getBytes();
                                                ByteBuf buf = Unpooled.buffer(bytes.length);
                                                buf.writeBytes(bytes);

                                                ctx.writeAndFlush(buf);

                                                line = br.readLine();
                                            }
                                        }

                                        @Override
                                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                            System.out.println(msg);
                                        }
                                    });
                        }
                    });

            //发起异步连接请求
            ChannelFuture f = b.connect(host, port).sync();

            //等待客户端链路关闭
            f.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new TimeClient().connect("127.0.0.1", 8086);
    }
}
