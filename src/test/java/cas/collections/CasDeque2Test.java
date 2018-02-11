package cas.collections;

import base.Dealer;
import base.Node;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * Created by guzy on 16/7/26.
 */
public class CasDeque2Test {
    CasDeque2<Integer> queueTwo=new CasDeque2<Integer>();
    int i=0;

    @Test
    public void test(){
        final CountDownLatch latch=new CountDownLatch(1);

        final CountDownLatch latchPrint=new CountDownLatch(180);

       // final CyclicBarrier barrier=new CyclicBarrier(179);

        for(;i<100;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int value=i+1;

                    try {
                        latch.await();
                        queueTwo.put(value);
                        latchPrint.countDown();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
        i=0;
        for(;i<80;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                   System.out.println("thread:"+ queueTwo.poll());
                    latchPrint.countDown();
                }
            }).start();
        }

        latch.countDown();
        try {
            latchPrint.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        i=0;
        System.out.println("===============================================");
        queueTwo.iterate(new Dealer<Node<Integer>>() {
            @Override
            public void deal(Node<Integer> o) {
                System.out.println(i+++":"+o.value);
            }
        });

    }
}
