package concurrent.myimpl;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

public class MyCyclicBarrierAqsTest1 {

    @Test
    public void test1() throws InterruptedException {
        final MyCyclicBarrierAqs barrier=new MyCyclicBarrierAqs(5);
        CountDownLatch latch=new CountDownLatch(5);
        for(int i=0;i<5;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        barrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("over!");
                    latch.countDown();
                }
            }).start();
        }
        latch.await();
    }

}
