package interview;

import java.util.concurrent.CountDownLatch;

public class Sync {

    CountDownLatch latch=new CountDownLatch(2);

    public synchronized void a(){
        try {
            latch.countDown();
            System.out.println("a1");
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("a");
    }

    public synchronized void b(){
        try {
            latch.countDown();
            System.out.println("b1");
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("b");
    }

    public static void main(String[] args) {
//        final Sync s=new Sync();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                s.b();
//            }
//        }).start();
//        s.a();

        System.out.println( InnerClassTest.StaticInnerClass.b);
    }
}
