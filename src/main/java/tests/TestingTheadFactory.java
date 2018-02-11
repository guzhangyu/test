package tests;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by guzy on 16/7/16.
 */
public class TestingTheadFactory implements ThreadFactory {

    public final AtomicInteger numCreated =new AtomicInteger();
    private final ThreadFactory factory= Executors.defaultThreadFactory();
    @Override
    public Thread newThread(Runnable r) {
        numCreated.incrementAndGet();
        return factory.newThread(r);
    }
}
