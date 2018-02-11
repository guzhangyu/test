package com.phei.netty.file;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.FileRegion;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * 文件服务handler
 * Created by guzy on 16/8/13.
 */
public class FileServerHandler extends SimpleChannelInboundHandler<String> {

    private String CR=System.getProperty("line.separator");

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, String msg) throws Exception {
        File file=new File(msg);
        if(!file.exists()){
            ctx.writeAndFlush("file not exists:"+file+CR);
        }if(!file.isFile()){
            ctx.writeAndFlush(" not a file:"+file+CR);
        }else{
            ctx.write(file+" "+file.length()+CR);
            RandomAccessFile randomAccessFile=new RandomAccessFile(msg,"r");
            FileRegion region=new DefaultFileRegion(randomAccessFile.getChannel(),0,randomAccessFile.length());

            ctx.writeAndFlush(region);
            randomAccessFile.close();
        }
    }
}
