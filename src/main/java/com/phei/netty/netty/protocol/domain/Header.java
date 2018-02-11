package com.phei.netty.netty.protocol.domain;

import java.util.Map;

/**
 * 协议消息头
 * Created by guzy on 16/8/15.
 */
public class Header {

    //校验码 oxabef
    private int crcCode;

    //消息长度
    private int length;

    private long sessionID;

    //0 业务请求,1 业务请求 ,2业务 ONE_WAY消息 ,3 握手请求,4 握手应答,5 心跳请求,6 心跳应答
    private int type;

    //优先级 0-255
    private int priority;

    //拓展消息头
    private Map<String,Object> attachment;


    public int getCrcCode() {
        return crcCode;
    }

    public void setCrcCode(int crcCode) {
        this.crcCode = crcCode;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public long getSessionID() {
        return sessionID;
    }

    public void setSessionID(long sessionID) {
        this.sessionID = sessionID;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Map<String, Object> getAttachment() {
        return attachment;
    }

    public void setAttachment(Map<String, Object> attachment) {
        this.attachment = attachment;
    }

    @Override
    public String toString() {
        return "Header{" +
                "crcCode=" + crcCode +
                ", length=" + length +
                ", sessionID=" + sessionID +
                ", type=" + type +
                ", priority=" + priority +
                ", attachment=" + attachment +
                '}';
    }
}
