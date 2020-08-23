//package com.test.netty.portunification;
//
//import io.netty.buffer.ByteBuf;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.ChannelPipeline;
//import io.netty.handler.codec.ByteToMessageDecoder;
//import io.netty.handler.codec.compression.ZlibCodecFactory;
//import io.netty.handler.codec.compression.ZlibWrapper;
//import io.netty.handler.codec.http.HttpContentCompressor;
//import io.netty.handler.codec.http.HttpRequestDecoder;
//import io.netty.handler.codec.http.HttpResponseEncoder;
//import io.netty.handler.ssl.SslContext;
//import io.netty.handler.ssl.SslHandler;
//
//import java.util.List;
//
//public class PortUnificationServerHandler extends ByteToMessageDecoder {
//
//    private final SslContext context;
//
//    private final boolean detectSsl;
//
//    private final boolean detectGzip;
//
//    public PortUnificationServerHandler(SslContext context) {
//        this(context, true, true);
//    }
//
//    public PortUnificationServerHandler(SslContext context, boolean detectSsl, boolean detectGzip) {
//        this.context = context;
//        this.detectSsl = detectSsl;
//        this.detectGzip = detectGzip;
//    }
//
//    @Override
//    protected void decode(ChannelHandlerContext context, ByteBuf in, List<Object> out) throws Exception {
//        // will use the first five bytes to detect a protocol.
//        if (in.readableBytes() < 5){
//            return;
//        }
//
//        if (isSsl(in)) {
//            enablesSsl(context);
//        }else {
//            final int magic1 = in.getUnsignedByte(in.readerIndex());
//            final int magic2 = in.getUnsignedByte(in.readerIndex() + 1);
//            if (isGzip(magic1, magic2)) {
//                enableGzip(context);
//            }else if(isHttp(magic1, magic2)) {
//                switchToHttp(context);
//            }else if(isFactorial(magic1)) {
//                switchToFactorial(context);
//            }else {
//                in.clear();
//                context.close();
//            }
//        }
//    }
//
//    private boolean isSsl(ByteBuf buf) {
//        if(detectSsl) {
//            return SslHandler.isEncrypted(buf);
//        }
//        return false;
//    }
//
//    private boolean isGzip(int magic1, int magic2) {
//        if (detectGzip) {
//            return magic1 == 31 && magic2 == 139;
//        }
//        return false;
//    }
//
//    private static boolean isHttp(int magic1, int magic2) {
//        return magic1 == 'G' && magic2 == 'E' ||
//                magic1 == 'P' && magic2 == 'O' ||
//                magic1 == 'P' && magic2 == 'U' ||
//                magic1 == 'D' && magic2 == 'E' ||
//                magic1 == 'O' && magic2 == 'P' ||
//                magic1 == 'H' && magic2 == 'E' ||
//                magic1 == 'P' && magic2 == 'A' ||
//                magic1 == 'T' && magic2 == 'R' ||
//                magic1 == 'C' && magic2 == 'O';
//    }
//
//    private static boolean isFactorial(int magic1){
//        return magic1 == 'F';
//    }
//
//    private void enablesSsl(ChannelHandlerContext context){
//        ChannelPipeline p = context.pipeline();
//        p.addLast("ssl", this.context.newHandler(context.alloc()));
//        p.addLast("unificationA", new PortUnificationServerHandler(this.context, false, detectGzip));
//        p.remove(this);
//    }
//
//    private void enableGzip(ChannelHandlerContext context){
//        ChannelPipeline p = context.pipeline();
//        p.addLast("gzipdeflater", ZlibCodecFactory.newZlibEncoder(ZlibWrapper.GZIP));
//        p.addLast("gzipinflater", ZlibCodecFactory.newZlibDecoder(ZlibWrapper.GZIP));
//        p.addLast("unificationB", new PortUnificationServerHandler(this.context, detectSsl, false));
//        p.remove(this);
//    }
//
//    private void switchToHttp(ChannelHandlerContext context){
//        ChannelPipeline p = context.pipeline();
//        p.addLast("decoder", new HttpRequestDecoder());
//        p.addLast("encoder", new HttpResponseEncoder());
//        p.addLast("deflater", new HttpContentCompressor());
//        p.addLast("handler", new HttpSnoopServerHandler());
//        p.remove(this);
//    }
//
//    private void switchToFactorial(ChannelHandlerContext context){
//        ChannelPipeline p = context.pipeline();
//        p.addLast("decoder", new BigIntegerDecoder());
//        p.addLast("encoder", new NumberEncoder());
//        p.addLast("handler", new FactorialServerHandler());
//        p.remove(this);
//    }
//}
