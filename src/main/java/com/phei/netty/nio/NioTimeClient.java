package com.phei.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by guzy on 16/8/1.
 */
public class NioTimeClient implements Runnable {


    public static void main(String[] args) throws IOException {
        new Thread(new NioTimeClient(8080)).start();
    }

    SocketChannel clientChannel=null;

    public static final int BUFFER_SIZE=1024;

    volatile boolean stop=false;

    Selector selector=null;

    int port;

    public NioTimeClient(int port){
        try {
            selector=Selector.open();
            clientChannel=SocketChannel.open();
            clientChannel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(11);
        }
        this.port=port;
    }

    private void doConnect() throws IOException {
//        Socket socket=clientChannel.socket();
//        socket.setReuseAddress(true);
//        socket.setReceiveBufferSize(BUFFER_SIZE);
//        socket.setSendBufferSize(BUFFER_SIZE);
        boolean connected=clientChannel.connect(new InetSocketAddress("127.0.0.1",port));
        if(connected){
            clientChannel.register(selector,SelectionKey.OP_READ);
            doWrite(clientChannel);
        }else{
            clientChannel.register(selector,SelectionKey.OP_CONNECT);
        }
    }

    private void handleInput(SelectionKey key) throws IOException {
        if(key.isValid()){
            //判断是否连接成功
            SocketChannel sc=(SocketChannel) key.channel();
            if(key.isConnectable()){
                if(sc.finishConnect()){
                    sc.register(selector,SelectionKey.OP_READ);
                    doWrite(sc);
                }else{
                    System.exit(1);//连接失败，进程退出
                }
            }
            if(key.isReadable()){
                ByteBuffer readBuffer=ByteBuffer.allocate(1024);
                int readBytes=sc.read(readBuffer);
                if(readBytes>0){
                    readBuffer.flip();
                    byte[] bytes=new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body=new String(bytes,"UTF-8");
                    System.out.println("Now is : "+body);
                    this.stop=true;
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

    private void doWrite(SocketChannel sc) throws IOException {
        byte[] req="QUERY TIME ORDER\n".getBytes();
        ByteBuffer writeBuffer=ByteBuffer.allocate(req.length);
        writeBuffer.put(req);
        writeBuffer.flip();
        sc.write(writeBuffer);
        if(!writeBuffer.hasRemaining()){
            System.out.println("Send order 2 server succeed.");
        }
    }

    @Override
    public void run() {
        try{
            doConnect();
        }catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }
        while(!stop){
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys=selector.selectedKeys();
                Iterator<SelectionKey> it=selectionKeys.iterator();
                SelectionKey key=null;
                while(it.hasNext()){
                    key=it.next();
                    it.remove();
                    try{
                        handleInput(key);
                    }catch (Exception e){
                        if(key!=null){
                            key.cancel();
                            SelectableChannel channel=key.channel();
                            if(channel!=null){
                                channel.close();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        if(selector!=null){
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
