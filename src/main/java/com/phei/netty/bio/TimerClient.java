package com.phei.netty.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by guzy on 16/7/31.
 */
public class TimerClient {

    public static void main(String[] args) {
        Integer port=8080;
        if(args.length>0){
            try{
                port=Integer.valueOf(args[0]);
            }catch (NumberFormatException e){

            }
        }
        Socket socket=null;
        BufferedReader br=null;
        PrintWriter pw=null;
        try {
            socket=new Socket("127.0.0.1",port);
            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw=new PrintWriter(socket.getOutputStream(),true);

            pw.println("QUERY TIME ORDER");
            String line=br.readLine();
           while(line!=null){
               if(line.equals("EOF")){
                   break;
               }
               System.out.println("get Info from server"+line);
               line=br.readLine();
           }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(pw!=null){
                pw.close();
                pw=null;
            }
            if(br!=null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                br=null;
            }
            if(socket!=null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                socket=null;
            }
        }
    }
}
