package com.phei.netty.aio;

/**
 * Created by guzy on 16/8/1.
 */
public class TimeServer {

    public static void main(String[] args) {
        int port=8080;
        AsyncTimeServerHandler timeServer=new AsyncTimeServerHandler(port);
        new Thread(timeServer,"AIO-AsyncTimeServerHandler-001").start();
    }
}
