package interview;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by guzy on 2018-03-29.
 */
public class SocketServer {

    public static void main(String[] args) {
        new SocketServer().oneServer();
    }

    public void oneServer(){
        try {
            ServerSocket server=new ServerSocket(5209);
            System.out.println("服务端启动");

            Socket socket=server.accept();
            String line;
            BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer=new PrintWriter(socket.getOutputStream());
            BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
            System.out.println("client:"+in.readLine());

            line=br.readLine();
            while(!line.equals("end")){
                writer.println(line);
                writer.flush();
                System.out.println("server:"+line);
                System.out.println("client:"+in.readLine());
                line=br.readLine();
            }

            writer.close();
            in.close();
            socket.close();
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
