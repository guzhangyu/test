import java.math.BigInteger;
import java.util.concurrent.*;

/**
 * 素数生产器
 * Created by guzy on 16/6/27.
 */
public class PrimeProducer extends Thread {

    BlockingQueue<BigInteger> queue=null;

    volatile Boolean cancelled=false;

    public PrimeProducer(BlockingQueue<BigInteger> queue){
        this.queue=queue;
    }

    /**
     * 不断放素数
     */
    public void run(){
        BigInteger p=BigInteger.ONE;
        while(!cancelled){
            try {
                p=p.nextProbablePrime();
                queue.put(p);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    /**
     * 获取下一个任务,打断后标记打断
     * @param queue
     * @return
     */
    public QuoteTask getNextTask(BlockingQueue<QuoteTask> queue){
        boolean interrupted=false;
        try{
            while(true){
                try {
                    return queue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    interrupted=true;
                }
            }
        }finally {
            if(interrupted){
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * 定时抛出异常
     * @param r
     * @param timeout
     * @param unit
     * @throws Throwable
     */
    public static void timedThrow(final Runnable r,long timeout,TimeUnit unit) throws Throwable {
        class RethrowableTask implements Runnable{
            private volatile Throwable t;
            public void run(){
                try{
                    r.run();
                }catch (Throwable t){
                    this.t=t;
                }
            }

            public void rethrow() throws Throwable {
                if(t!=null){
                    throw t;
                }
            }
        }

        RethrowableTask task=new RethrowableTask();
        final Thread thread=new Thread(task);
        thread.start();

        //定时抛出引起异常
        ScheduledExecutorService cancelExec= Executors.newScheduledThreadPool(20);
        cancelExec.schedule(new Runnable() {
            @Override
            public void run() {
                thread.interrupt();
            }
        },timeout,unit);
        thread.join(unit.toMillis(timeout));


        task.rethrow();
    }

    /**
     * 定时获取内容
     * @param r
     * @param timeout
     * @param unit
     * @throws InterruptedException
     */
    public static void timedGet(Runnable r,long timeout,TimeUnit unit) throws InterruptedException{
        Future<?> task=taskExec.submit(r);
        try {
            task.get(timeout,unit);
        } catch (ExecutionException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally{
            task.cancel(true);
        }
    }

    static ExecutorService taskExec=Executors.newCachedThreadPool();
}
