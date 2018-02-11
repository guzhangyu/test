import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by guzy on 16/4/22.
 */
public class QueueTimeServerHandler implements Runnable {


    public static final Queue<Socket> socketQueue=new ConcurrentLinkedQueue<Socket>();

    @Override
    public void run() {
       while(!socketQueue.isEmpty()){
           Socket socket=socketQueue.poll();
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
               }

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
}
