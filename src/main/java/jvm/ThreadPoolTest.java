package jvm;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolTest {

    public static void main(String[] args) {
        ThreadPoolExecutor poolExecutor=new ThreadPoolExecutor(5,10,200, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(5));

        for(int i=0;i<16;i++){
            poolExecutor.execute(new MyTask(i));
            System.out.println(String.format("queueSize:%d,activeCount:%d,taskCount:%d,completedCount:%d,poolSize:%d",poolExecutor.getQueue().size(),poolExecutor.getActiveCount(),poolExecutor.getTaskCount(),poolExecutor.getCompletedTaskCount(),poolExecutor.getPoolSize()));
        }
        poolExecutor.shutdown();
    }
}

class MyTask implements Runnable{

    private int i;

    public MyTask(int i) {
        this.i = i;
    }

    @Override
    public void run() {
        System.out.println("begin "+i);
        try {
            Thread.sleep(1000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("end "+i);
    }
}
