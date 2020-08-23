package netty4.decode;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class SubReqServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        super.channelRead(ctx, msg);
        SubscribeReq req = (SubscribeReq)msg;
        System.out.println("operateRabbit request[" + msg + "]");
        ctx.writeAndFlush(getRespById(req.getSubReqID()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        ctx.flush();
    }

    private SubscribeResp getRespById(int id){
        SubscribeResp resp = new SubscribeResp();
        resp.setSubReqID(id);
        resp.setDesc("desc");
        resp.setRespCode(0);
        return resp;
    }
}
