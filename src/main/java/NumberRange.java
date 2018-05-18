import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by guzy on 16/6/22.
 */
public class NumberRange {

    private final AtomicInteger lower = new AtomicInteger(0);

    private final AtomicInteger upper = new AtomicInteger(0);

    //final Object concurrent.lock=new Object();

    ReentrantLock lock=new ReentrantLock();

    Condition condition=null;

    public NumberRange(){
        condition=lock.newCondition();
    }

    public void check(){
        lock.lock();
        try{
            int low=lower.get();
            int up=upper.get();
            if(low>up){
                throw new IllegalArgumentException(String.format("data error[%d,%d]",low,up));
            }
        }finally {
            lock.unlock();
        }
    }

    public void setLower(int i){

       // ArrayBlockingQueue a=null;

//        synchronized (concurrent.lock){
//            try {
//                concurrent.lock.wait();
//
//                concurrent.lock.notify();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

        try{
            lock.lock();

            if(i>upper.get()){
                throw new IllegalArgumentException("can't set lower to "+i+" > upper");
            }
            lower.set(i);
        }finally {
            lock.unlock();
        }
    }

    public void setUpper(int i){
//        synchronized (concurrent.lock){
//            try {
//                concurrent.lock.wait();
//                i
//                concurrent.lock.notify();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

       try{
           lock.lock();

           if(i<lower.get()){
               throw new IllegalArgumentException("can't set upper to "+i+" < lower");
           }
           upper.set(i);
       }finally{
           lock.unlock();
       }
    }

    public static int i=0;

    public static void main(String[] args) {
        final NumberRange numberRange=new NumberRange();
        int n=1000;
        final CountDownLatch latch=new CountDownLatch(n);
        for(i=0;i<n;i++){
             new Thread(new Runnable() {
                @Override
                public void run() {
                    int value=new Random().nextInt(100);
                    try {
                        if(i%3==0){
                            latch.await();
                            numberRange.setLower(value);
                        }else if(i%3==1){
                            latch.await();
                            numberRange.setUpper(value);
                        }else {
                            latch.await();
                            numberRange.check();
//                            if(!numberRange.check()){
//                                System.out.println("--------------- error --------------------------");
//                            }
                        }
                        //System.out.println("end");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
            latch.countDown();
        }
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    latch.await();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                numberRange.setLower(0);
//            }
//        }).start();
//        latch.countDown();
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    latch.await();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                numberRange.setUpper(10);
//            }
//        }).start();
//        latch.countDown();
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    latch.await();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                numberRange.setLower(5);
//            }
//        }).start();
//        latch.countDown();
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    latch.await();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                numberRange.setUpper(4);
//            }
//        }).start();
//        latch.countDown();

    }
}
