package tests;

import java.util.Date;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by guzy on 16/7/16.
 */
public class PutTakeTest {

    private static final ExecutorService pool= Executors.newCachedThreadPool();
    private final AtomicInteger putSum=new AtomicInteger(0);
    private final AtomicInteger takeSum=new AtomicInteger(0);
    private final CyclicBarrier barrier;
    private final BoundedBuffer<Integer> bb;
    private final int nTrails,nPairs;
    private BarrierTimer timer=new BarrierTimer();

    public static void main(String[]args){
        try {
            new PutTakeTest(10,10,1000).testDiffArgs();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //.test1();//示例参数
       // pool.shutdown();
    }

    PutTakeTest(int capacity,int nPairs,int nTrails){
        this.nTrails=nTrails;
        this.nPairs=nPairs;
        this.bb=new BoundedBuffer<Integer>(capacity);
        this.barrier=new CyclicBarrier(nPairs*2+1,timer);
    }

    public void test1(){
        try{
            timer.clear();
            for(int i=0;i<nPairs;i++){
                pool.execute(new Producer());
                pool.execute(new Consumer());
            }
            barrier.await();//所有线程就绪
            barrier.await();//所有线程执行完成

            System.out.println("thought:"+(timer.getTime()/(nPairs*nTrails))+" ns/item");
            assert(putSum.get()==takeSum.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    public void testDiffArgs() throws InterruptedException {
        int tpt=100000;//每个线程中的测试次数
        for(int cap=1;cap<=1000;cap*=10){
            System.out.println("capacity:"+cap);
            for(int pairs=1;pairs<=128;pairs*=2){
            //int pairs=3;
                PutTakeTest t=new PutTakeTest(cap,pairs,tpt);
                System.out.print(new Date()+"Pairs: " + pairs + "\t");
                t.test1();

                System.out.print("\t");
                Thread.sleep(1000);
                t.test1();
                System.out.println();
                Thread.sleep(1000);

            }
        }
        pool.shutdown();
    }

    public void test2(){
        new PutTakeTest(10,10,1000).test1();//示例参数
        pool.shutdown();
    }

    public void test(){
        try{
            for(int i=0;i<nPairs;i++){
                pool.execute(new Producer());
                pool.execute(new Consumer());
            }
            barrier.await();//所有线程就绪
            barrier.await();//所有线程执行完成

            assert(putSum.get()==takeSum.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    class Producer implements Runnable{
        public void run(){
           try{
               long seed=(this.hashCode() ^ System.nanoTime());
               int sum=0;
               barrier.await();
               for(int i=nTrails;i>0;--i){
                   bb.put((int)seed);
                   sum+=seed;
                   seed= xorShift(seed);
               }
               putSum.getAndAdd(sum);
               barrier.await();
           } catch (InterruptedException e) {
               e.printStackTrace();
           } catch (BrokenBarrierException e) {
               e.printStackTrace();
           }
        }
    }

    class Consumer implements Runnable{
        @Override
        public void run() {
            try{
                barrier.await();
                int sum=0;
                for(int i=nTrails;i>0;--i){
                    Integer cur=bb.take();
                    if(cur!=null){
                        sum+=cur;
                    }
                }
                takeSum.getAndAdd(sum);
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
    long xorShift(long y){
        y^=(y<<6);
        y^=(y>>>21);
        y^=(y<<7);
        return y;
    }

    public class BarrierTimer implements Runnable{
        private boolean started;

        private long startTime,endTime;

        public synchronized void run(){
            long t=System.nanoTime();
            if(!started){
                started=true;
                startTime=t;
            }else{
                endTime=t;
            }
        }

        public synchronized void clear(){
            started=false;
        }

        public synchronized long getTime(){
            return endTime-startTime;
        }
    }
}


