package com.phei.netty.aio;

/**
 * Created by guzy on 16/8/1.
 */
public class TimeClient {

    public static void main(String[] args) {
        new Thread(new AsyncTimeClientHandler("127.0.0.1",8080)).start();
    }
}
