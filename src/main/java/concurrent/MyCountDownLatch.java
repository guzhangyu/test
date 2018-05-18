package concurrent;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyCountDownLatch {

    private AtomicInteger counts;

    private Lock lock=new ReentrantLock();
    private Condition condition=null;

    public MyCountDownLatch(int counts) {
        this.counts=new AtomicInteger(counts);
        condition=lock.newCondition();
    }

    public void countDown(){
        if(counts.get()<=0){
            throw new IllegalArgumentException("countDown times exceed!");
        }
        int result=counts.decrementAndGet();
        if(result<0){
            throw new IllegalArgumentException("countDown times exceed!");
        }
        if(result==0){
            lock.lock();
            condition.signalAll();
            lock.unlock();
        }
    }

    public void await() throws InterruptedException {
        if(counts.get()<=0){
            return;
        }
        lock.lock();
        condition.await();
        lock.unlock();
    }

    public static void main(String[] args) throws InterruptedException {
        final MyCountDownLatch countDownLatch=new MyCountDownLatch(1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Thread.sleep(1000l);
        new Thread(new Runnable() {
            @Override
            public void run() {
                countDownLatch.countDown();
            }
        }).start();
        countDownLatch.await();
        countDownLatch.await();
        System.out.println("hha ");
    }
}
