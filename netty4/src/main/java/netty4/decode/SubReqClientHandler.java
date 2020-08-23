package netty4.decode;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class SubReqClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i=0; i<10; i++){
            ctx.write(getSubReq(i));
        }
        ctx.flush();
    }

    private SubscribeReq getSubReq(int i){
        SubscribeReq req = new SubscribeReq();
        req.setSubReqID(i);
        req.setAddress("ruian");
        req.setPhoneNumber("18657704499");
        req.setProductName("电视");
        req.setUserName("谷章雨");
        return req;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        super.channelRead(ctx, msg);
        System.out.println("get msg:" + msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        super.channelReadComplete(ctx);
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        ctx.flush();
    }
}
