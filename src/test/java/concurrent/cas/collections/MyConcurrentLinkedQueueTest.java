package concurrent.cas.collections;

import org.junit.Test;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class MyConcurrentLinkedQueueTest {

    /**
     * 已经有一个元素a，
     * 此时加入一个新元素b，在set了tail之后，尚未设置tail的next
     * 此时消费掉元素a，设置head为a的next，即null
     * 导致丢失一个元素b
     * @throws InterruptedException
     */
    @Test
    public void test1() throws InterruptedException {
        MyConcurrentLinkedQueue<String> queue=new MyConcurrentLinkedQueue<>();
        CountDownLatch latch=new CountDownLatch(2);
        queue.offer("2");
        new Thread(new Runnable() {
            @Override
            public void run() {
                queue.offer("1");
                latch.countDown();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                String s=null;
                while((s=queue.poll())!=null){
                    System.out.println(s);
                    System.out.println(String.format("poll time1:%s",new Date()));
                }
                latch.countDown();
            }
        }).start();
        latch.await();

        new Thread(new Runnable() {
            @Override
            public void run() {
                String s=null;
                while((s=queue.poll())!=null){
                    System.out.println(s);
                    System.out.println(String.format("poll time2:%s",new Date()));
                }
            }
        }).start();
    }

    /**
     * 只有一个元素a
     * poll到最后一个元素，比较head与tail，一致，在set tail之前，
     * offer的方法set 了tail
     * 导致丢失此后添加的所有node
     */
    @Test
    public void test2() throws InterruptedException {
        MyConcurrentLinkedQueue<String> queue=new MyConcurrentLinkedQueue<>();
        CountDownLatch latch=new CountDownLatch(2);
        queue.offer("2");

        new Thread(new Runnable() {
            @Override
            public void run() {
                String s=null;
                while((s=queue.poll())!=null){
                    System.out.println(s);
                    System.out.println(String.format("poll time1:%s",new Date()));
                }
                latch.countDown();
            }
        }).start();


        CountDownLatch offerLatch=new CountDownLatch(1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                queue.offer("1");
                latch.countDown();
                offerLatch.countDown();
            }
        }).start();
        latch.await();


        try {
            offerLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String s=null;
        while((s=queue.poll())!=null){
            System.out.println(s);
            System.out.println(String.format("poll time2:%s",new Date()));
        }
    }
}
