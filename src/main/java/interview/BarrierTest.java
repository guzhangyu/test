package interview;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class BarrierTest {

    //final static CyclicBarrier barrier=new CyclicBarrier(60);
    static CountDownLatch latch=new CountDownLatch(60);

    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        java.lang.String a;
        for(int i=0;i<50;i++){
            for(int j=0;j<10;j++){
                System.out.println("main "+j);
                latch.countDown();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for(int j=0;j<50;j++){
                        System.out.println("sub "+j);
                        latch.countDown();
                    }
                }
            }).start();
            latch.await();
            latch=new CountDownLatch(60);
        }
    }
}
