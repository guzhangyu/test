package jvm;

import java.util.concurrent.CountDownLatch;

/**
 * Created by guzy on 16/10/21.
 */
public class SynAddRunnable implements Runnable {

    static CountDownLatch latch=new CountDownLatch(100);

    int a,b;

    public SynAddRunnable(int a, int b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public void run() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (Integer.valueOf(a)){
            synchronized (Integer.valueOf(b)){
                System.out.println(a+b);
            }
        }
    }

    public static void main(String[] args) {
        for(int i=0;i<100;i++){
            latch.countDown();
            new Thread(new SynAddRunnable(1,2)).start();
            new Thread(new SynAddRunnable(2,1)).start();
        }
    }
}
