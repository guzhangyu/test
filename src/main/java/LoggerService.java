import java.io.PrintWriter;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by guzy on 16/6/30.
 */
public class LoggerService {

    private final BlockingQueue<String> queue;

    private final LoggerThread loggerThread;
    private boolean isShutdown;
    private int reservations;

    public void stop(){
        synchronized (this){
            isShutdown=true;
        }
        loggerThread.interrupt();
    }

    public void start(){
        loggerThread.start();
    }

    public void log(String msg){
        try {
            synchronized (this){
                if(isShutdown){
                    return;
                }
                reservations++;
            }
            queue.put(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public LoggerService(PrintWriter writer){
        loggerThread=new LoggerThread(writer);
        queue=new ArrayBlockingQueue<String>(2);
    }

    class LoggerThread extends Thread{

        PrintWriter writer;

        public LoggerThread(PrintWriter writer){
            this.writer=writer;
        }

        public void run(){
            try {
               while(true){
                   synchronized (LoggerService.this){
                       if(isShutdown){
                           if(reservations>0){
                               reservations--;
                           }else{
                               break;
                           }
                       }
                   }
                   writer.println(queue.take());
               }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                writer.close();
            }
//            synchronized (LoggerService.this){
//
//            }
        }

    }

    public static void main(String[] args) {
        final LoggerService loggerService=new LoggerService(new PrintWriter(System.out));
        loggerService.start();
        loggerService.log("1");
        loggerService.log("2");
        loggerService.log("3");
        new Thread(new Runnable() {
            @Override
            public void run() {
                loggerService.stop();
            }
        }).start();
        loggerService.log("4");
    }
}
