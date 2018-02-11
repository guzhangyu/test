package com.phei.netty.bio.simple;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by guzy on 16/4/22.
 */
public class TimeServer {

    public static void main(String[]args){
        ServerSocket serverSocket=null;
        try {
            serverSocket=new ServerSocket(4233);
            while(true){
                Socket socket= serverSocket.accept();
                new Thread(new TimeServerHandler(socket)).start();
                //System.out.println(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(serverSocket!=null){
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
