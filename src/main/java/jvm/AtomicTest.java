package jvm;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by guzy on 17/1/10.
 */
public class AtomicTest {

    public static AtomicInteger race=new AtomicInteger(0);

    public static void increase(){
        race.incrementAndGet();
    }

    public static final int THREADS_COUNT=20;

    public static void main(String[] args) {
        Thread[] threads=new Thread[THREADS_COUNT];
        for(int i=0;i<THREADS_COUNT;i++){
            threads[i] =new Thread(new Runnable() {
                @Override
                public void run() {
                    for(int i=0;i<10000;i++){
                        increase();
                    }
                }
            });
            threads[i].start();
        }

        while(Thread.activeCount()>2){
           // Thread.dumpStack();
           // System.out.println();
            Thread.yield();
        }
        System.out.println(race);
    }
}
