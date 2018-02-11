package com.phei.netty.netty.protocol.domain;

/**
 * Created by guzy on 16/8/15.
 */
public class NettyMessage {

    //消息头
    private Header header;

    //消息主体
    private Object body;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString(){
        return String.format("header:%s,body:%s",header,body);
    }
}
