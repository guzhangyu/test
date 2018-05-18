package concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class MyReadWriteLock {

    class Sync extends AbstractQueuedSynchronizer{

//        volatile int write;
//
//        long writeOffset;

        AtomicInteger write=new AtomicInteger(0);

        public Sync(){
            //writeOffset=super
        }

        @Override
        protected boolean tryAcquire(int arg) {
            return compareAndSetState(0,1) &&
                    write.compareAndSet(0,1);
        }

        @Override
        protected boolean tryRelease(int arg) {
            return true;
        }

        @Override
        protected int tryAcquireShared(int arg) {
            if(write.get()==0){
                return 1;
            }
            return -1;
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
           //if(write.get()==)
            return true;
        }
    }

    Sync sync=new Sync();
    
    Lock readLock=new MyReadLock(),writeLock=new MyWriteLock();

    public class MyReadLock extends AbstractQueuedSynchronizer implements Lock{

        @Override
        public void lock() {
            //MyReadWriteLock.this.sync
            MyReadWriteLock.this.sync.acquireShared(1);
        }

        @Override
        public void lockInterruptibly() throws InterruptedException {

        }

        @Override
        public boolean tryLock() {
            return false;
        }

        @Override
        public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
            return false;
        }

        @Override
        public void unlock() {
            releaseShared(1);
        }

        @Override
        public Condition newCondition() {
            return null;
        }

//        public int getState(){
//            return super.getState();
//        }
    }

    public class MyWriteLock extends AbstractQueuedSynchronizer implements Lock{

        @Override
        protected boolean tryAcquire(int arg) {
            return true;
           // return readLock;
        }

        @Override
        protected boolean tryRelease(int arg) {
            return super.tryRelease(arg);
        }

        @Override
        public void lock() {
            acquire(1);
        }

        @Override
        public void lockInterruptibly() throws InterruptedException {

        }

        @Override
        public boolean tryLock() {
            return false;
        }

        @Override
        public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
            return false;
        }

        @Override
        public void unlock() {
            release(1);
        }

        @Override
        public Condition newCondition() {
            return null;
        }
    }



}
