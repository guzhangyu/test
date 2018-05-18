package concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class MyReadWriteLock2 {

    class Sync extends AbstractQueuedSynchronizer{
        @Override
        protected boolean tryAcquire(int arg) {
            return super.tryAcquire(arg);
        }

        @Override
        protected boolean tryRelease(int arg) {
            return super.tryRelease(arg);
        }

        @Override
        protected int tryAcquireShared(int arg) {
            return super.tryAcquireShared(arg);
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            return super.tryReleaseShared(arg);
        }
    }

    Sync sync=new Sync();

    Lock readLock=new MyReadLock(),writeLock=new MyWriteLock();

    public class MyReadLock extends AbstractQueuedSynchronizer implements Lock{

        @Override
        public void lock() {
            //MyReadWriteLock.this.sync
            acquireShared(1);
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
