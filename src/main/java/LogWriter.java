import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by guzy on 16/6/28.
 */
public class LogWriter {
    private final BlockingQueue<String> queue;

    final LoggerThread logger;

    public LogWriter(PrintWriter writer){
        this.queue=new LinkedBlockingQueue<String>(1);
        this.logger=new LoggerThread(writer);
    }

    public void start(){
        logger.start();
    }

    public void log(String msg)throws InterruptedException{
        queue.put(msg);
    }

    class LoggerThread extends Thread{
        private final PrintWriter writer;

        public LoggerThread(PrintWriter writer){
            this.writer=writer;
        }

        public void run(){
            try {
                while(true){
                    writer.println(queue.take());
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                writer.close();
            }

        }
    }

    public static void main(String[] args) throws InterruptedException {
        LogWriter logWriter=new LogWriter(new PrintWriter(System.out));
        logWriter.start();
        logWriter.log("Hello World!");
        logWriter.logger.interrupt();
        logWriter.log("h1");
        logWriter.log("h2");
    }
}

