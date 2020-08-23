package netty4.http.file;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

public class HttpFileServer {

    private static final String DEFAULT_URL  = "/netty4";

    public void run(String host, final String url, int port){
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast("http-decoder", new HttpRequestDecoder())
                                    .addLast("http-aggregator", new HttpObjectAggregator(65536))
                                    .addLast("http-encoder", new HttpRequestEncoder())
                                    .addLast("http-chunked", new ChunkedWriteHandler())
                                    .addLast("fileServerHandler", new HttpFileServerHandler(url));
                        }
                    });

            ChannelFuture future = b.bind(host, port).sync();
            System.out.println("文件目录服务启动，地址是: http://" + host + ":" + port + url);

            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new HttpFileServer().run("127.0.0.1", DEFAULT_URL, 8084);
    }
}
