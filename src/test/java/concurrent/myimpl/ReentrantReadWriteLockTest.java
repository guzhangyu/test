package concurrent.myimpl;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLockTest {

    ReentrantReadWriteLock readWriteLock=new ReentrantReadWriteLock();
    Lock readLock=readWriteLock.readLock();
    Lock writeLock=readWriteLock.writeLock();

    CountDownLatch latch;
    /**
     * w r
     */
    @Test
    public void test2() throws InterruptedException {
       latch =new CountDownLatch(1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                writeLock.lock();
                latch.countDown();

                try {
                    Thread.sleep(2000l);//关键之处，让read有足够的时间去lock
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                writeLock.unlock();
                System.out.println("write unlock ");
            }
        }).start();

        latch.await();
        readLock.lock();
        System.out.println("main read lock ");
        readLock.unlock();
        System.out.println("main read unlock");
    }

    /**
     * r wi r
     * 写意向的时候，读锁也不能加
     */
    @Test
    public void test3() throws InterruptedException {
        latch =new CountDownLatch(1);
        CountDownLatch latch2=new CountDownLatch(1);

        new Thread(new Runnable() {
            @Override
            public void run() {
                readLock.lock();
                System.out.println("run1 read lock 1");//1
                latch.countDown();

                try {
                    latch2.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                readLock.unlock();
                System.out.println("run1 read unlock 4");//4
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("run2 write lock begin 2");//2
                writeLock.lock();
                System.out.println("run2 write lock 5");//2

                writeLock.unlock();
                System.out.println("run2 write unlock 6");//5
            }
        }).start();

        latch.await();
        //----------- 关键之处
        Thread.sleep(1000l);//此处没有触发的await，只能估摸着线程2已经在等待队列中了
        System.out.println("main read lock begin 3");//3
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                latch2.countDown();
            }
        }).start();

        readLock.lock();
        System.out.println("main read lock 7");//6

        readLock.unlock();
        System.out.println("main read unlock 8");
    }

    /**
     * r wi r
     * 写意向的时候，读锁也不能加
     */
    @Test
    public void test6() throws InterruptedException {
        latch =new CountDownLatch(1);
        CountDownLatch latch2=new CountDownLatch(1);

        new Thread(new Runnable() {
            @Override
            public void run() {
                readLock.lock();
                System.out.println("run1 read lock 1");//1
                latch.countDown();

                try {
                    latch2.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                readLock.unlock();
                System.out.println("run1 read unlock 4");//4
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("run2 write lock begin 2");//2
                writeLock.lock();
                System.out.println("run2 write lock 5");//2

                writeLock.unlock();
                System.out.println("run2 write unlock 6");//5
            }
        }).start();

        latch.await();
        //----------- 关键之处
        Thread.sleep(1000l);//此处没有触发的await，只能估摸着线程2已经在等待队列中了
        System.out.println("main read lock begin 3");//3
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                latch2.countDown();
            }
        }).start();

        readLock.lock();
        System.out.println("main read lock 7");//6

        readLock.unlock();
        System.out.println("main read unlock 8");
    }

    /**
     * r w
     */
    @Test
    public void test4() throws InterruptedException {
        readLock.lock();
        latch=new CountDownLatch(1);

        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                writeLock.lock();
                System.out.println("write lock 3");
                writeLock.unlock();
                System.out.println("write unlock 4");
            }
        });
        t.start();

        latch.countDown();
        System.out.println("main read lock 1");

        try {
            Thread.sleep(2000l);//关键之处，让write有足够的时间去lock
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        readLock.unlock();
        System.out.println("main read unlock 2");
        t.join();
    }

    /**
     * w ri w
     * 读意向的时候，写锁不受影响
     */
    @Test
    public void test5() throws InterruptedException {
        latch =new CountDownLatch(1);
        CountDownLatch latch2=new CountDownLatch(1);

        new Thread(new Runnable() {
            @Override
            public void run() {
                writeLock.lock();
                System.out.println("run1 write lock 1");//1
                latch.countDown();

                try {
                    latch2.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                writeLock.unlock();
                System.out.println("run1 write unlock 4");//4
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    latch.await();
                    //----------- 关键之处
                    Thread.sleep(1000l);//此处没有触发的await，只能估摸着主线程的read锁已经在等待队列中了
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("run2 write lock begin 3");//2
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000l);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        latch2.countDown();
                    }
                }).start();
                writeLock.lock();
                System.out.println("run2 write lock 5");//2

                writeLock.unlock();
                System.out.println("run2 write unlock 6");//5
            }
        }).start();

        latch.await();
        System.out.println("main read lock begin 2");//3
        readLock.lock();
        System.out.println("main read lock 7");//6

        readLock.unlock();
        System.out.println("main read unlock 8");
    }
}
