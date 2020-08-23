package com.test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer1;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MyReadWriteLock {

    private Sync sync=new Sync();

    private Lock readLock,writeLock;

    public MyReadWriteLock() {
        readLock=new MyReadLock();
        writeLock=new MyWriteLock();
    }

    public Lock readLock(){
        return readLock;
    }

    public Lock writeLock(){
        return writeLock;
    }


//    class Sync extends AbstractQueuedSynchronizer{
//
//        private final static int WRITE_MASK=1024;
//
//        boolean isHeldExclusively=false;
//
//        @Override
//        protected boolean tryAcquire(int arg) {
//            if(getState()==0){
//                compareAndSetState(0,WRITE_MASK);
//                isHeldExclusively=true;
//                return true;
//            }
//            return false;
//        }
//
//        @Override
//        protected boolean tryRelease(int arg) {
//            return super.tryRelease(arg);
//        }
//
//        @Override
//        protected int tryAcquireShared(int arg) {
//            if(isHeldExclusively()){
//                return -1;
//            }
//            if(getState()/WRITE_MASK>0){
//                return 0;
//            }
//            return getState()%WRITE_MASK;
//        }
//
//        @Override
//        protected boolean tryReleaseShared(int arg) {
//            return super.tryReleaseShared(arg);
//        }
//
//        @Override
//        protected boolean isHeldExclusively() {
//            return isHeldExclusively;
//        }
//    }

    class MyWriteLock implements Lock{

        @Override
        public void lock() {
            sync.acquire(1);
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
            sync.release(1);
        }

        @Override
        public Condition newCondition() {
            return null;
        }
    }


    class MyReadLock implements Lock{

        @Override
        public void lock() {
            sync.acquireShared(1);
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
            sync.releaseShared(1);
        }

        @Override
        public Condition newCondition() {
            return null;
        }
    }

    class Sync extends AbstractQueuedSynchronizer1 {

        private final static int WRITE_MASK=1024;

        @Override
        protected boolean tryAcquire(int arg) {
            //因为加写锁的时候，一定没有读锁占有
            while(getState()==0){
                if(compareAndSetState(0,WRITE_MASK)){
                    return true;
                }
            }
            return false;
        }

        @Override
        protected int tryAcquireShared(int arg) {
            while(!isHeldByWriteLock()){//如果现在不是写锁状态
                if(!getExclusiveQueuedThreads().isEmpty()){//如果有等待的写锁
                    return -1;
                }

                if(compareAndSetState(getState(),getState()+1)){//如果争抢成功
                    //apparentlyFirstQueuedIsExclusive
                    //如果第一个等待的线程为写锁
                    if(writeLockAtTopOfWaitingQueue()){
                        return 0;
                    }
                    return getSharedQueuedThreads().size();
                }
            }
            return -1;
        }

        private boolean writeLockAtTopOfWaitingQueue() {
            return !getExclusiveQueuedThreads().isEmpty() && getFirstQueuedThread()==getExclusiveQueuedThreads().iterator().next();
        }

        protected boolean isHeldByWriteLock() {
            return getState()/WRITE_MASK>0;
        }

        @Override
        protected boolean tryRelease(int arg) {
            if(getExclusiveOwnerThread()==Thread.currentThread()){
                compareAndSetState(WRITE_MASK,0);
                return true;
            }
            return false;
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            if(getExclusiveOwnerThread()==Thread.currentThread()){
                compareAndSetState(getState(),getState()-1);
                return true;
            }
            return false;
        }

    }

//    public static void main(String[] args) throws InterruptedException {
////        MyReadWriteLock myReadWriteLock=new MyReadWriteLock();
////        Lock readLock=myReadWriteLock.readLock();
////        Lock writeLock=myReadWriteLock.writeLock();
////        writeLock.lock();
////        Thread.sleep(1000l);
////        Thread thread=new Thread(new Runnable() {
////            @Override
////            public void run() {
////                readLock.lock();
////            }
////        });
////        thread.start();
////        thread.join();
////        System.out.println("aha");
//
//
//    }

    public static void main(String[] args) {
        ReentrantReadWriteLock readWriteLock=new ReentrantReadWriteLock();
        ReentrantReadWriteLock.ReadLock readLock=readWriteLock.readLock();
        ReentrantReadWriteLock.WriteLock writeLock=readWriteLock.writeLock();

        new Thread(new Runnable() {
            @Override
            public void run() {
                writeLock.lock();
                writeLock.unlock();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                readLock.lock();
//                latch.countDown();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                readLock.lock();
//                latch.countDown();
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                readLock.lock();
                System.out.println("haha");
            }
        }).start();
    }
}
