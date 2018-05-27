package concurrent.myimpl;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class MyReadWriteLock {

    class Sync extends AbstractQueuedSynchronizer{

        AtomicInteger read=new AtomicInteger(0);

        /**
         * 写锁前要判断读锁是否有
         * 并且在加锁的过程中可能有读锁产生，此时要回滚
         * @param arg
         * @return
         */
        @Override
        protected boolean tryAcquire(int arg) {
            if(read.get()==0 && compareAndSetState(0,1)){
                if(read.get()>0){
                    boolean result=compareAndSetState(1,0);//此时没有竞争
                    assert result:"出异常啦！";
                    return false;
                }
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
            //return write.get()==0 && compareAndSetState(0,1) &&  write.compareAndSet(0,1);
            //return super.getExclusiveQueuedThreads().size()==0 && compareAndSetState(0,1);
        }

        /**
         * 写锁释放就是普通的释放锁
         * @param arg
         * @return
         */
        @Override
        protected boolean tryRelease(int arg) {
            //return compareAndSetState(1,0) &&  write.compareAndSet(1,0);
            if(compareAndSetState(1,0)){
                setExclusiveOwnerThread(null);
                return true;
            }
            return false;
        }

        /**
         * 获取共享锁的时候，要判断没有写锁以及写意向锁
         * 之后要将读锁的数量加一
         * @param arg
         * @return
         */
        @Override
        protected int tryAcquireShared(int arg) {
            int result= (getState()==0 && this.getExclusiveQueuedThreads().size()==0 && read.incrementAndGet()>0)?1:-1;
            return result;
        }

        /**
         * 释放读锁的时候要将读数量减1，第一个判断条件可以不要
         * @param arg
         * @return
         */
        @Override
        protected boolean tryReleaseShared(int arg) {
            return getState()==0 && read.decrementAndGet()>=0;
        }
    }

    Sync sync=new Sync();
    
    Lock readLock=new MyReadLock(),writeLock=new MyWriteLock();

    public class MyReadLock implements Lock{

        @Override
        public void lock() {
            //MyReadWriteLock.this.sync
            MyReadWriteLock.this.sync.acquireShared(1);
        }

        @Override
        public void lockInterruptibly() throws InterruptedException {
            MyReadWriteLock.this.sync.acquireSharedInterruptibly(1);
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
            MyReadWriteLock.this.sync.releaseShared(1);
        }

        @Override
        public Condition newCondition() {
            return null;
        }
    }

    public class MyWriteLock implements Lock{

        @Override
        public void lock() {
            MyReadWriteLock.this.sync.acquire(1);
        }

        @Override
        public void lockInterruptibly() throws InterruptedException {
            MyReadWriteLock.this.sync.acquireInterruptibly(1);
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
            MyReadWriteLock.this.sync.release(1);
        }

        @Override
        public Condition newCondition() {
            return null;
        }
    }
}
