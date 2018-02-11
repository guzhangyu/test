package com.phei.netty.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * nio server
 * Created by guzy on 16/8/1.
 */
public class NioTimeServer implements Runnable {

    public static void main(String[] args) throws IOException {
        int port=8080;
        if(args.length>0){
            try{
                port=Integer.valueOf(args[0]);
            }catch (NumberFormatException e){

            }
        }
        new Thread( new NioTimeServer(port)).start();
    }

    ServerSocketChannel acceptorSvr;

    Selector selector;

    volatile boolean  stop;


    public NioTimeServer(int port){
        try {
            selector=Selector.open();
            acceptorSvr=ServerSocketChannel.open();
            acceptorSvr.socket().bind(new InetSocketAddress(InetAddress.getByName("127.0.0.1"),port));
            acceptorSvr.configureBlocking(false);
            acceptorSvr.register(selector,SelectionKey.OP_ACCEPT);
            System.out.println("the time server is start in port:"+port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void run() {
       while(!stop){
           try {
               selector.select(1000);
               Set<SelectionKey> selectionKeys=selector.selectedKeys();
               Iterator<SelectionKey> it=selectionKeys.iterator();
               while(it.hasNext()){
                   SelectionKey key=it.next();
                   it.remove();
                   try{
                       handleInput(key);
                   }catch (Exception e){
                       if(key!=null){
                           key.cancel();
                           if(key.channel()!=null){
                               key.channel().close();
                           }
                       }
                   }
               }
           } catch (ClosedChannelException e) {
               e.printStackTrace();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }

        //多路复用器关闭后，所有注册在上面的channel和pipe等资源都会被自动去注册并关闭，所以不需要重复释放资源
        if(selector!=null){
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey key) throws IOException {
        if(key.isValid()){
            //处理新接入的请求消息
            if(key.isAcceptable()){
                //accept the new connection
                ServerSocketChannel ssc=(ServerSocketChannel)key.channel();
                SocketChannel sc=ssc.accept();
                sc.configureBlocking(false);
                //add the new connection to the selector
                sc.register(selector,SelectionKey.OP_READ);
            }
            if(key.isReadable()){
                //read the data
                SocketChannel sc=(SocketChannel)key.channel();
                ByteBuffer readBuffer=ByteBuffer.allocate(1024);
                int readBytes=sc.read(readBuffer);
                if(readBytes>0){
                    readBuffer.flip();
                    byte[] bytes=new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body=new String(bytes,"UTF-8");
                    System.out.println("The time server receive order : "+body);
                    String currentTime="QUERY TIME ORDER".equalsIgnoreCase(body)?new Date().toString():"BAD ORDER";
                    doWrite(sc,currentTime);
                }else if(readBytes<0){
                    //对端链路关闭
                    key.cancel();
                    sc.close();
                }else{
                    //读到0字节，忽略
                }
            }
        }
    }

    public void stop(){
        this.stop=true;
    }

    private void doWrite(SocketChannel channel,String response) throws IOException {
        if(response!=null && response.trim().length()>0){
            byte[] bytes=response.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            channel.write(writeBuffer);
        }
    }
}
