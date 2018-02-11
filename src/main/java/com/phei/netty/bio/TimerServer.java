package com.phei.netty.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by guzy on 16/7/31.
 */
public class TimerServer {


    public static void main(String[] args) throws IOException {
        int port=8080;

        //ExecutorService es= Executors.newFixedThreadPool(10);

        if(args!=null && args.length>0){
           try{
               port = Integer.valueOf(args[0]);
           }catch (NumberFormatException e){

           }
        }
        ServerSocket server=null;
        try{
            server=new ServerSocket(port);
            System.out.println("The time server is start in port : "+port);
            Socket socket=null;
            TimeServerHandlerExecutePool es=new TimeServerHandlerExecutePool(50,10000);
            while(true){
                socket=server.accept();
                es.execute(new TimeServerHandler(socket));
            }
        }finally {
            if(server!=null){
                System.out.println("The time server close");
                server.close();
                server=null;
            }
        }
    }
}
