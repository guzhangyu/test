package com.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SemaphoreTest {

    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        final Semaphore semp = new Semaphore(5);
        for(int index=0; index<50; index++){
            final int NO = index;
            Runnable run = () -> {
                try {
                    semp.acquire();
                    System.out.println("Accessing: " + NO);
                    Thread.sleep((long)(Math.random() * 6000));
                    semp.release();
                    System.out.println("-----------------" + semp.availablePermits());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
            exec.execute(run);
        }
        exec.shutdown();
    }
}
