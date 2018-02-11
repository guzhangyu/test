package com.phei.netty.bio.simple;

import java.io.*;
import java.net.Socket;

/**
 * Created by guzy on 16/4/22.
 */
public class TimeServerHandler implements Runnable {

    Socket socket;

    public TimeServerHandler(Socket socket){
        this.socket=socket;
    }

    @Override
    public void run() {
        //System.out.println(socket);
        if(socket==null){
            return;
        }
        BufferedReader br=null;
        PrintWriter pw=null;
        try {
            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw = new PrintWriter(socket.getOutputStream(),true);

            String line=null;
            while((line=br.readLine())!=null){
                pw.println(line);
                if(line.length()==0){
                    break;
                }
            }
            pw.println("");

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (br!=null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(pw!=null){
                    pw.close();
                }
            }
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
