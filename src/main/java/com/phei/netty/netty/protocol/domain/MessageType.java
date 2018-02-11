package com.phei.netty.netty.protocol.domain;

/**
 * Created by guzy on 16/8/15.
 */
public enum MessageType {

    LOGIN_REQ(2),LOGIN_RESP(3),HEART_BEAT_REQ(4),HEART_BEAT_RESP(5);

    int value;

    MessageType(int value){
        this.value=value;
    }

    public int value(){
        return value;
    }

}
