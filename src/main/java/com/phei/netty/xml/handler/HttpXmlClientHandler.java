package com.phei.netty.xml.handler;

import com.phei.netty.xml.codec.HttpXmlRequest;
import com.phei.netty.xml.codec.HttpXmlResponse;
import com.phei.netty.xml.pojo.Customer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Arrays;

/**
 * Created by guzy on 16/8/12.
 */
public class HttpXmlClientHandler extends SimpleChannelInboundHandler<HttpXmlResponse> {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Customer customer=new Customer();
        customer.setCustomerNumber(1l);
        customer.setFirstName("f");
        customer.setMiddleNames(Arrays.asList("2"));
        customer.setLastName("l");
        HttpXmlRequest request=new HttpXmlRequest(null,customer);
        ctx.writeAndFlush(request);

        //super.channelActive(ctx);
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, HttpXmlResponse msg) throws Exception {
        System.out.println("headers:"+msg.getHttpResponse().headers().names());
        System.out.println("body:"+msg.getResult());
    }
}
