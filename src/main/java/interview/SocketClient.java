package interview;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by guzy on 2018-03-29.
 */
public class SocketClient {

    public static void main(String[] args) {
        try {
            Socket socket=new Socket("127.0.0.1",5209);
            String line;
            BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer=new PrintWriter(socket.getOutputStream());
            BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
            writer.println("古筝版那个");
            writer.flush();
            System.out.println("server:"+in.readLine());

            line=br.readLine();
            while(!line.equals("end")){
                writer.println(line);
                writer.flush();
                System.out.println("client:"+line);
                System.out.println("server:"+in.readLine());
                line=br.readLine();
            }

            writer.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
