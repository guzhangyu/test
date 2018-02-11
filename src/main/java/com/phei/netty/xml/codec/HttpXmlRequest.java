package com.phei.netty.xml.codec;

import io.netty.handler.codec.http.FullHttpRequest;

/**
 * Created by guzy on 16/8/11.
 */
public class HttpXmlRequest {

    private FullHttpRequest request;

    private Object body;

    public FullHttpRequest getRequest() {
        return request;
    }

    public void setRequest(FullHttpRequest request) {
        this.request = request;
    }


    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }


    public HttpXmlRequest(FullHttpRequest request, Object body) {
        this.request = request;
        this.body = body;
    }
}
