package interview;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;

public class TicketSeller {

    private Queue<Integer> queue=new ArrayBlockingQueue<Integer>(20);

    public TicketSeller(){
        for(int i=0;i<20;i++){
            queue.offer(i);
        }
    }

    public Integer sell(){
        return queue.poll();
    }

    public static void main(String[] args) {
        TicketSeller ticketSeller=new TicketSeller();
        CountDownLatch latch=new CountDownLatch(4);
         for(int i=0;i<4;i++){
             new Thread(new Runnable() {
                 @Override
                 public void run() {
                     try {
                         latch.await();
                         for(int j=0;j<5;j++){
                             System.out.println(String.format("%s,%s",Thread.currentThread().getId(), ticketSeller.sell()));;
                         }
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                     }
                 }
             }).start();
             latch.countDown();
         }
    }
}
