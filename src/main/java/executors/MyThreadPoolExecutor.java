package executors;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 自定义类型的线程池（fixed single reject）
 * Created by guzy on 16/7/10.
 */
public class MyThreadPoolExecutor extends ThreadPoolExecutor {

    private MyThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory,handler);
    }

    /**
     * 不限制队列里等待的线程个数
     * @param nThreads
     * @param exceptionHandler
     * @param handler
     * @return
     */
    public static MyThreadPoolExecutor newFixedMTPool(int nThreads,Thread.UncaughtExceptionHandler exceptionHandler,RejectedExecutionHandler handler){
        ThreadFactory threadFactory=new ExceptionHandleThreadFactory(exceptionHandler);
        return new MyThreadPoolExecutor(nThreads,nThreads,0,TimeUnit.MILLISECONDS,new LinkedBlockingDeque<Runnable>(),threadFactory,handler);
    }


    /**
     * 队列里等待的最多有1个
     * @param nThreads 可以同时用的线程个数
     * @param exceptionHandler
     * @param handler
     * @return
     */
    public static ExecutorService newSingleMTPool(int nThreads,Thread.UncaughtExceptionHandler exceptionHandler,RejectedExecutionHandler handler){
        ThreadFactory threadFactory=new ExceptionHandleThreadFactory(exceptionHandler);
        return new MyThreadPoolExecutor(nThreads,nThreads,0,TimeUnit.MILLISECONDS,new LinkedBlockingDeque<Runnable>(1),threadFactory,handler);
    }

    /**
     * 带有拒绝策略（原线程忽略）的线程池
     * @param nThreads
     * @return
     */
    public static ExecutorService newFixedCRRejectMTPool(int nThreads){
        return new ThreadPoolExecutor(nThreads,nThreads,0,TimeUnit.MILLISECONDS,new LinkedBlockingDeque<Runnable>(1),new ThreadPoolExecutor.DiscardPolicy());
    }

    ThreadLocal<Long> begin=new ThreadLocal<Long>();

    AtomicInteger runTasks=new AtomicInteger();

    AtomicLong useTime=new AtomicLong();

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        begin.set(System.currentTimeMillis());
        super.beforeExecute(t, r);
    }

    @Override
    protected void terminated() {
        System.out.println("average time:"+(useTime.get()/runTasks.get()));
        super.terminated();
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        Long useTime=System.currentTimeMillis()-begin.get();
        runTasks.incrementAndGet();
        this.useTime.getAndAdd(useTime);
        System.out.println("use time:"+useTime);
        super.afterExecute(r, t);
    }

    static class ExceptionHandleThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;
        private final Thread.UncaughtExceptionHandler exceptionHandler;

        ExceptionHandleThreadFactory(Thread.UncaughtExceptionHandler exceptionHandler) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "pool-" +
                    poolNumber.getAndIncrement() +
                    "-thread-";
            this.exceptionHandler=exceptionHandler;
        }

        public Thread newThread(final Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            t.setUncaughtExceptionHandler(exceptionHandler);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }

    public static void main(String[] args) {
        final ExecutorService executor
                =MyThreadPoolExecutor.newFixedCRRejectMTPool(2);
//                newSingleMTPool(2, new UEHLogger(), new RejectedExecutionHandler() {
//            @Override
//            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
//                System.out.println(((Thread)r).getId()+"---rejected!");
//            }
//        });
       // =MyThreadPoolExecutor.newFixedCRRejectMTPool(2);
//        executor.submit(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println(1 / 0);
//            }
//        });
        final CountDownLatch latch=new CountDownLatch(2);
        final CountDownLatch latch1=new CountDownLatch(1);
        executor.execute(new Thread(){
            @Override
            public long getId() {
                return  1l;
            }

            @Override
            public void run() {
                try {
                    System.out.println("my id1:"+Thread.currentThread().getId());
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("waiting1 .....");

            }
        });

        executor.execute(new Thread(){
            @Override
            public long getId() {
                return 2l;
            }

            @Override
            public void run() {
                try {
                    System.out.println("my id2:"+Thread.currentThread().getId());
                    latch.await();
                    latch1.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("waiting2 .....");
            }
        });

        executor.execute(new Thread(){
            @Override
            public long getId() {
                return 3l;
            }

            @Override
            public void run() {
                try {
                    System.out.println("my id3:"+Thread.currentThread().getId());
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("waiting3 .....");
            }
        });

        executor.execute(new Thread(){
            @Override
            public long getId() {
                return 4l;
            }

            @Override
            public void run() {
                try {
                    System.out.println("my id4:"+Thread.currentThread().getId());
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("waiting4 .....");
            }
        });

        latch.countDown();
        latch.countDown();
        latch1.countDown();

//
//        try {
//            Thread.sleep(10000l);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        latch.countDown();
       // latch.countDown();
//        Future<?> futureTask=executor.submit(new Thread(){
//            @Override
//            public long getId() {
//                return 3l;
//            }
//
//            @Override
//            public void run() {
//                try{
//                    System.out.println("my id3:"+Thread.currentThread().getId());
//                    System.out.println(1 / 0);
//                }catch (Throwable e){
//                    System.out.println("error");
//                    e.printStackTrace();
//                    // System.exit(1);
//                }
//            }
//        });
//
//        try {
//            futureTask.get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                executor.shutdownNow();
                System.out.println("I invoked!!");
            }
        }));

        executor.shutdown();
    }

}
