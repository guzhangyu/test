package com.phei.netty.xml.handler;

import com.phei.netty.xml.codec.HttpXmlRequest;
import com.phei.netty.xml.pojo.Customer;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.Arrays;

/**
 * Created by guzy on 16/8/12.
 */
public class HttpXmlServerHandler extends ChannelHandlerAdapter {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        cause.printStackTrace();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        Customer customer=new Customer();
        customer.setCustomerNumber(1l);
        customer.setFirstName("f");
        customer.setMiddleNames(Arrays.asList("2"));
        customer.setLastName("l");
        HttpXmlRequest request=new HttpXmlRequest(null,customer);
        ctx.writeAndFlush(request);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //super.channelRead(ctx, msg);
        HttpXmlRequest request=(HttpXmlRequest)msg;
        System.out.println("received headers:"+request.getRequest().headers().names());
        System.out.println("operateRabbit from client:"+request.getBody());
    }
}
