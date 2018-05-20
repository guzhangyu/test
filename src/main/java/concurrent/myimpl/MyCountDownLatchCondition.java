package concurrent.myimpl;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyCountDownLatchCondition implements MyCountDownLatch{

    private AtomicInteger counts;

    private Lock lock=new ReentrantLock();
    private Condition condition=null;

    public MyCountDownLatchCondition(int counts) {
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
}
