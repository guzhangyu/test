package concurrent.tests;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by guzy on 16/7/16.
 */
public class BoundedBufferTest {

    public static void main(String[] args) {
        final BoundedBuffer<Integer> bb=new BoundedBuffer<Integer>(4);
        final Random random=new Random();
        final CountDownLatch latch=new CountDownLatch(1);

        for(int i=0;i<20;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        latch.await();
                        int rd=random.nextInt(30);
                        if(rd%2==0){
                            bb.put(rd);
                            //System.out.println("put:"+rd);
                        }else if(rd%2==1){
                            bb.take();
                            //System.out.println("take:"+bb.take());
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        latch.countDown();

        final CountDownLatch latch1=new CountDownLatch(1);
        for(int i=0;i<80;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        latch1.await();
                        int rd=random.nextInt(30);
                        if(rd%3==0){
                            bb.put(rd);
                            //System.out.println("put:"+rd);
                        }else if(rd%3==1){
                            bb.take();
                            //System.out.println("take:"+bb.take());
                        }else{
                            bb.printInfo();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        latch1.countDown();
    }

    public void testPoolExpansion() throws InterruptedException {
        int MAX_SIZE=10;
        ExecutorService exec= Executors.newFixedThreadPool(MAX_SIZE);
        for(int i=0;i<10*MAX_SIZE;i++){
            exec.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(Long.MAX_VALUE);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
//        for(int i=0;i<20 && threadFactory.numCreated.get()<MAX_SIZE;i++)
//            Thread.sleep(100);
//        assertEquals(threadFactory.numCreated.get(),MAX_SIZE);
        exec.shutdown();
    }
}
