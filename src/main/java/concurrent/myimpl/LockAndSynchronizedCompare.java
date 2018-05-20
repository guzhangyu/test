package concurrent.myimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockAndSynchronizedCompare {
    static int j=0;
    static CountDownLatch latch=new CountDownLatch(10);

    public static void main(String[] args) throws InterruptedException {
        Lock lock=new ReentrantLock();

        List<Thread> threads=new ArrayList<>();
        for(int i=0;i<10000;i++){
            threads.add(new Thread(new Runnable() {
                @Override
                public void run() {
                    lock.lock();
                    j++;
                    lock.unlock();
                    latch.countDown();
                }
            }));
        }

        long end = execute(latch, threads);

        threads.clear();
        latch=new CountDownLatch(10);
        for(int i=0;i<10000;i++){
            threads.add(new Thread(new Runnable() {
                @Override
                public void run() {
                    synchronized (LockAndSynchronizedCompare.class){
                        j++;
                    }
                    latch.countDown();
                }
            }));
        }

        long syncEnd=execute(latch, threads);

        System.out.println(String.format("concurrent.lock:%d,synchronized:%d",end,syncEnd));
    }


    private static long execute(CountDownLatch latch, List<Thread> threads) throws InterruptedException {
        long start=System.currentTimeMillis();
        for(Thread thread:threads){
            thread.start();
        }
        latch.await();//等到所有结果得到
        return System.currentTimeMillis()-start;
    }
}
