package interview;

import java.util.concurrent.*;

/**
 * Created by guzy on 2018-04-03.
 */
public class ExecutorsTest {

    class Test implements Runnable{

        @Override
        public void run() {
            System.out.println(String.format("%s is running ",Thread.currentThread().getId()));
            try {
                Thread.sleep(1000l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(String.format("%s is end ",Thread.currentThread().getId()));
        }
    }

    public static void main(String[] args) {
        ExecutorsTest executorsTest=new ExecutorsTest();
        ScheduledExecutorService executor= Executors.newScheduledThreadPool(3);
        executor.scheduleWithFixedDelay(executorsTest.new Test(), 1, 1, TimeUnit.SECONDS);
        executor.scheduleWithFixedDelay(executorsTest.new Test(),1, 1, TimeUnit.SECONDS); executor.scheduleWithFixedDelay(executorsTest.new Test(),1, 1, TimeUnit.SECONDS); executor.scheduleWithFixedDelay(executorsTest.new Test(),1, 1, TimeUnit.SECONDS); executor.scheduleWithFixedDelay(executorsTest.new Test(),1, 1, TimeUnit.SECONDS);
        //executor.schedule(executorsTest.new Test(), 1, TimeUnit.SECONDS);executor.schedule(executorsTest.new Test(), 1, TimeUnit.SECONDS);executor.schedule(executorsTest.new Test(), 1, TimeUnit.SECONDS);executor.schedule(executorsTest.new Test(), 1, TimeUnit.SECONDS);
       // executor.shutdown();
    }
}
