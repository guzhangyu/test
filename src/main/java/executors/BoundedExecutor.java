package executors;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;

/**
 * Created by guzy on 16/7/11.
 */
public class BoundedExecutor {

    Executor executor;

    Semaphore semaphore;

    public BoundedExecutor(Executor executor,int bound){
        this.executor=executor;
        this.semaphore=new Semaphore(bound);
    }

    public void submitTask(final Runnable command) throws InterruptedException {
        semaphore.acquire();
        try{
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        command.run();
                    }finally {
                        semaphore.release();
                    }
                }
            });
        }catch (RejectedExecutionException e){
            semaphore.release();
        }
    }
}
