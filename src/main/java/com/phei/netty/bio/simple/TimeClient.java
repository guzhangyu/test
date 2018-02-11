package com.phei.netty.bio.simple;

import java.io.*;
import java.net.Socket;

/**
 * Created by guzy on 16/4/22.
 */
public class TimeClient {

    public static void main(String[]args){
        for(int i=0;i<10;i++){
            System.out.println(i);
            requestServer();
        }

    }

    private static void requestServer() {
        Socket socket= null;
        PrintWriter pw=null;
        BufferedReader br=null;

        try {
            socket = new Socket("127.0.0.1",4233);

            OutputStream os=socket.getOutputStream();
            pw=new PrintWriter(os,true);
            pw.println("haha\nheihei\n");

            InputStream is=socket.getInputStream();
            br=new BufferedReader(new InputStreamReader(is));

            String line=null;
            while((line=br.readLine())!=null){
                System.out.println("get line:"+line);
                if(line.length()==0){
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(socket!=null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(pw!=null){
                    pw.close();
                    if(br!=null){
                        try {
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
