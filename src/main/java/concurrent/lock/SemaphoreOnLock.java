package concurrent.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by guzy on 16/7/23.
 */
public class SemaphoreOnLock {

    private final Lock lock=new ReentrantLock();

    private final Condition permitsAvail=lock.newCondition();

    private int permits;

    SemaphoreOnLock(int initialPermits){
       // AbstractQueuedSynchronizer
        try{
            lock.lock();
            permits=initialPermits;
        }finally {
            lock.unlock();
        }
    }

    public void acquire() throws InterruptedException {
        try{
            lock.lock();

            while(permits<=0){
                permitsAvail.await();
            }

            permits--;
        }finally {
            lock.unlock();
        }
    }

    public void release(){
        try{
            lock.lock();

            permits++;
            if(permits==1){
                permitsAvail.signal();
            }
        }finally {
            lock.unlock();
        }
    }
}
