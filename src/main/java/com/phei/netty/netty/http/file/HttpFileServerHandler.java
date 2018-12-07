package com.phei.netty.netty.http.file;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Pattern;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpHeaders.*;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * http 文件服务器处理器
 * Created by guzy on 16/8/4.
 */
public class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    /**
     * 起始字符串
     */
    private String url;

    public HttpFileServerHandler(String url) {
        this.url = url;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        //如果解码成功
        if(!msg.getDecoderResult().isSuccess()){
            sendError(ctx, BAD_REQUEST);
            return;
        }
        //如果非get请求
        if(msg.getMethod()!= GET){
            sendError(ctx,METHOD_NOT_ALLOWED);
            return;
        }
        //如果路径不正确
        final String uri=msg.getUri();
        final String path=sanitizeUri(uri);
        if(path==null){
            sendError(ctx,FORBIDDEN);
            return;
        }

        //判断文件的可读性
        File file=new File(path);
        if(file.isHidden() || !file.exists()){
            sendError(ctx,NOT_FOUND);
            return;
        }
        if(file.isDirectory()){
            if(uri.endsWith("/")){
                sendListing(ctx,file);
            }else{
                sendRedirect(ctx,uri+"/");
            }
            return;
        }
        if(!file.isFile()){
            sendError(ctx,FORBIDDEN);
            return;
        }

        RandomAccessFile randomAccessFile=null;
        try{
            randomAccessFile=new RandomAccessFile(file,"r");//以只读方式打开
        }catch (FileNotFoundException fnfe){
            sendError(ctx,NOT_FOUND);
            return;
        }

        //设置请求头
        HttpResponse response=new DefaultHttpResponse(HTTP_1_1,OK);
        long fileLength=randomAccessFile.length();
        setContentLength(response,fileLength);
        setContentTypeHeader(response,file);
        if(isKeepAlive(msg)){
            response.headers().set(CONNECTION, Values.KEEP_ALIVE);
        }
        ctx.write(response);

        //发送文件内容
        ChannelFuture sendFileFuture=ctx.write(new ChunkedFile(randomAccessFile,0,fileLength,8192),ctx.newProgressivePromise());
        sendFileFuture.addListener(new ChannelProgressiveFutureListener() {
            @Override
            public void operationProgressed(ChannelProgressiveFuture channelProgressiveFuture, long progress, long total) throws Exception {
                if(total<0){//total unknown
                    System.err.println("Transfer progress:"+progress);
                }else{
                    System.err.println("Transfer progress:"+progress+"/"+total);
                }
            }

            @Override
            public void operationComplete(ChannelProgressiveFuture channelProgressiveFuture) throws Exception {
                System.out.println("Transfer complete.");
            }
        });

        //内容发送结束处理器
        ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        if(!isKeepAlive(msg)){
            lastContentFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }

    /**
     * 设置文件类型头部信息
     * @param response
     * @param file
     */
    private void setContentTypeHeader(HttpResponse response, File file) {
        response.headers().set(CONTENT_TYPE,new MimetypesFileTypeMap().getContentType(file.getPath()));
                //.set(CONTENT_TYPE,"multipart/file");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if(ctx.channel().isActive()){
            sendError(ctx,INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 错误请求地址
     */
    private static final Pattern INSECURE_URI=Pattern.compile(".*[<>&\"].*");

    /**
     * 允许的文件名称
     */
    private static final Pattern ALLOW_FILE_NAME =Pattern.compile("[A-Za-z0-9][-_A-Za-z0-9\\.]*");

    /**
     * 展示文件列表
     * @param ctx
     * @param dir
     */
    private static void sendListing(ChannelHandlerContext ctx,File dir){
        FullHttpResponse response=new DefaultFullHttpResponse(HTTP_1_1,OK);
        response.headers().set(CONTENT_TYPE,"text/html;charset=UTF-8");
        StringBuilder buf =new StringBuilder();
        String dirPath =dir.getPath();
        buf.append("<!DOCTYPE html><title>").append(dirPath).append("下目录").append("</title><body>");
        buf.append("<ul>");
        for(File f:dir.listFiles()){
            if(f.isHidden() || !f.canRead() || !ALLOW_FILE_NAME.matcher(f.getName()).matches()){
                continue;
            }
            buf.append("<li>").append(String.format("<a href=\"%s\">%s</a>", f.getName(),f.getName())).append("</li>");
        }
        buf.append("</ul></body>");
        buf.append("</html>");

        ByteBuf byteBuf= Unpooled.copiedBuffer(buf, CharsetUtil.UTF_8);
        response.content().writeBytes(byteBuf);
        byteBuf.release();
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 请求跳转
     * @param ctx
     * @param newUri
     */
    private void sendRedirect(ChannelHandlerContext ctx,String newUri){
        FullHttpResponse response=new DefaultFullHttpResponse(HTTP_1_1,FOUND);
        response.headers().set(LOCATION,newUri);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private void sendError(ChannelHandlerContext ctx,HttpResponseStatus error){
//        FullHttpResponse response=new DefaultFullHttpResponse(HTTP_1_1,OK);
//        response.headers().set(CONTENT_TYPE,"text/html;charset=UTF-8");
     //  response.content().writeBytes(Unpooled.copiedBuffer(String.format("error:%s",error.code()), CharsetUtil.UTF_8));
        FullHttpResponse response=new DefaultFullHttpResponse(HTTP_1_1,error,Unpooled.copiedBuffer("Failure: "+error.toString(),CharsetUtil.UTF_8));
        response.headers().set(CONTENT_TYPE,"text/plain; charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
    /**
     * 将uri转为目录路径
     * @param uri
     * @return
     */
    private String sanitizeUri(String uri){
        try {
            uri = URLDecoder.decode(uri,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            try{
                uri=URLDecoder.decode(uri,"ISO-8859-1");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
                throw new Error();
            }
        }
        if(!uri.startsWith(url)){
            return null;
        }
        if(!uri.startsWith("/")){
            return null;
        }
        uri = uri.replace('/',File.separatorChar);
        if(uri.contains(File.separator+'.') || uri.contains('.'+File.separator) || uri.startsWith(".")
                || uri.endsWith(".") || INSECURE_URI.matcher(uri).matches()){
            return null;
        }
        return "/Users/guzy/IdeaProjects/myTest/src/main/java"+File.separator+uri;
        //return System.getProperty("user.dir")+"/../myTest"+File.separator+uri;
    }


}
