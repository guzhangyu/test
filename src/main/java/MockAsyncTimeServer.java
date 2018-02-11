import com.phei.netty.bio.simple.TimeServerHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by guzy on 16/4/22.
 */
public class MockAsyncTimeServer {

    static ExecutorService executor= Executors.newCachedThreadPool();

    public static void main(String[]args){
        ServerSocket serverSocket=null;
        try {
            serverSocket=new ServerSocket(4233);
            while(true){
                Socket socket= serverSocket.accept();
                //new Thread(new com.phei.netty.bio.simple.TimeServerHandler(socket)).start();
                //System.out.println(socket);
                executor.execute(new TimeServerHandler(socket));
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
