package executors;

import java.util.concurrent.ThreadFactory;

/**
 * Created by guzy on 16/7/11.
 */
public class MyThreadFactory implements ThreadFactory{

    private final String poolName;

    public MyThreadFactory(String poolName){
        this.poolName=poolName;
    }

    public Thread newThread(Runnable runnable){
        return new MyThread(runnable,poolName);
    }
}
