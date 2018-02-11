package com.phei.netty.bio;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by guzy on 16/7/31.
 */
public class TimeServerHandlerExecutePool {

    private ExecutorService es;

    public TimeServerHandlerExecutePool(int maxPoolSize,int queueSize){
        es=new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),maxPoolSize,
                120L, TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(queueSize));
    }

    public void execute(Runnable task){
        es.execute(task);
    }
}
