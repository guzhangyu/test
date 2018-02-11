package com.phei.netty.netty.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guzy on 16/8/3.
 */
public class TestSubscribeReqProto {

    private static byte[] encode(SubscribeReqProto.SubscribeReq req){
        return req.toByteArray();
    }

    private static SubscribeReqProto.SubscribeReq decode(byte[] body) throws InvalidProtocolBufferException {
        return SubscribeReqProto.SubscribeReq.parseFrom(body);
    }

    private static SubscribeReqProto.SubscribeReq createSubscribeReq(){
        SubscribeReqProto.SubscribeReq.Builder builder=SubscribeReqProto.SubscribeReq.newBuilder();
        builder.setSubReqID(1);
        builder.setUserName("Lilinfeng");
        builder.setProductName("Netty Book");
        List<String> address=new ArrayList<String>();
        address.add("NanJing HuHuaTai");
        address.add("BeiJing LiiuliChang");
        address.add("ShenZhen HongShuLin");
        builder.addAllAddress(address);
        return builder.build();
    }

    public static void main(String[] args) throws InvalidProtocolBufferException {
        SubscribeReqProto.SubscribeReq req=createSubscribeReq();
        System.out.println("Before encode:"+req.toString());

        SubscribeReqProto.SubscribeReq req2=decode(encode(req));
        System.out.println("after decode:"+req.toString());
        System.out.println("Assert equal : "+req2.equals(req));
    }
}
