package concurrent.myimpl;

import concurrent.myimpl.MyCountDownLatch;
import concurrent.myimpl.MyCountDownLatchCS;
import concurrent.myimpl.MyCountDownLatchCondition;
import org.junit.Test;

public class MyCountDownLatchTest {

    @Test
    public void test() throws InterruptedException {
        final MyCountDownLatchCS countDownLatch=new MyCountDownLatchCS(3);
        testCountDown(countDownLatch);
    }

    @Test
    public void test2() throws InterruptedException {
        final MyCountDownLatchCondition countDownLatch=new MyCountDownLatchCondition(3);
        testCountDown(countDownLatch);
    }

    private void testCountDown(MyCountDownLatch countDownLatch) throws InterruptedException {
        for(int i=0;i<3;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000l);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    countDownLatch.countDown();
                    System.out.println("countDown ");
                }
            }).start();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("run await ");
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("run get");
            }
        }).start();

        System.out.println("main await");
        countDownLatch.await();
        System.out.println("main get");
    }
}
