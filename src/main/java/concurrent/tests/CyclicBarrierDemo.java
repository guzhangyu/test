package concurrent.tests;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierDemo {

    final CyclicBarrier barrier;

    final int MAX_TASK;

    public CyclicBarrierDemo(int cnt){
        barrier=new CyclicBarrier(cnt+1);
        MAX_TASK=cnt;
    }

    public void doWork(){
        new Thread(){
            public void run(){
                try {
                    Thread.sleep(1000l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                waitForNext();
            }
        }.start();
    }

    private void doWithIndex(int index) {
        System.out.println("haha:"+index);
        if(index==MAX_TASK/3){
            System.out.println("left 30%");
        }else if(index==MAX_TASK/2){
            System.out.println("left 50%");
        }else if(index==0){
            System.out.println("finish");
        }
    }

    public void waitForNext(){
        try {
            doWithIndex(barrier.await());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        final int count=10;
        CyclicBarrierDemo demo=new CyclicBarrierDemo(count);
        for(int i=0;i<10;i++){
            demo.doWork();
            if((i+1)%count==0){
                demo.waitForNext();
            }
        }
    }


}
