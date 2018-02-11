package com.phei.netty.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

/**
 * Created by guzy on 16/7/31.
 */
public class TimeServerHandler implements Runnable {

    private Socket socket;

    public TimeServerHandler(Socket socket){
        this.socket=socket;
    }

    @Override
    public void run() {
        BufferedReader in=null;
        PrintWriter out=null;
        try {
            in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new PrintWriter(socket.getOutputStream(),true);
            String currentTime=null;
            String body=in.readLine();
            while(body!=null){
                System.out.println("The time server receive order:"+body);
                currentTime="QUERY TIME ORDER".equalsIgnoreCase(body)?new Date().toString():"BAD ORDER";
                out.println(currentTime);
                out.println("EOF");
                body=in.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if(out!=null){
                out.close();
                out=null;
            }
            if(socket!=null){
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                socket=null;
            }
        }
    }
}
