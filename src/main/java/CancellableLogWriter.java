import java.io.PrintWriter;
import java.util.concurrent.*;

/**
 * Created by guzy on 16/6/30.
 */
public class CancellableLogWriter {

    final BlockingQueue<String> queue;

    final CancellableLogThread logThread;

    final ExecutorService executor= Executors.newSingleThreadExecutor();

    //CountDownLatch latch=new CountDownLatch(3);

    //PrintWriter writer;

    public CancellableLogWriter(PrintWriter writer){
        //this.writer=writer;
        queue=new LinkedBlockingQueue<String>(1);
        logThread=new CancellableLogThread(writer);
    }

    public void start(){
        logThread.start();
    }

    public void cancel(){
        this.cancel=true;
        executor.shutdown();
        try {
            executor.awaitTermination(1000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void log(final String log){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                innerLog(log);
            }
        });
    }

    private void innerLog(String log){
        if(cancel){
            return;
        }
        try {
            queue.put(log);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    volatile Boolean cancel=false;

    volatile int times=0;

    class CancellableLogThread extends Thread{

        PrintWriter writer=null;

        public CancellableLogThread(PrintWriter writer){
            this.writer=writer;
        }

        public void run(){
            try {
               while(!cancel || queue.size()>0){
                   //Thread.sleep(1000l);
//                   if(times==2){
//                       cancel();
//                   }
//                   System.out.println("enter");
//                   times++;
                   writer.println(queue.take());
               }
            } catch (InterruptedException e) {
                e.printStackTrace();
               // Thread.currentThread().interrupt();
               // this.interrupt();
                cancel();
                run();
            }finally {
//                while(queue.size()>0){
//                    try {
//                        writer.println(queue.take());
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
                writer.close();
            }
        }
    }

    public static void main(String[] args) {
        final CancellableLogWriter clw=new CancellableLogWriter(new PrintWriter(System.out));
        clw.start();
        clw.log("haha");
        clw.log("haha");
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
                clw.logThread.interrupt();
//            }
//        }).start();
        try {
            Thread.sleep(1000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clw.log("hehe");

      //  clw.log("dede");
       // clw.cancel();
//        clw.log("dd");
//        clw.log("ee");

    }
}
