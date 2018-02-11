import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by guzy on 16/6/22.
 */
public class SafeListWrapper {
    private Vector vector=new Vector();

    private ReentrantLock lock=new ReentrantLock();

    private ReentrantLock lock1=new ReentrantLock();

    public  int findCount(Object x){
        try{
            lock1.lock();
            int count=0;
            int pre=vector.indexOf(x);
            while(pre >=0){
                count++;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                pre=vector.indexOf(x,pre+1);
            }
            return count;
        }finally {
            lock1.unlock();
        }
    }

    public void remove(Object x){
        vector.remove(x);
    }

    public  void addIfAbsent(Object x){
//        synchronized (vector){
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println("enter");
//        }
        try{
            lock.lock();
            if(!vector.contains(x)){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                vector.add(x);
            }
        }finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        MultiThread multiThread=new MultiThread(1000);
        final SafeListWrapper safeListWrapper=new SafeListWrapper();
        multiThread.exec(new Exec() {
            @Override
            public void exec(int i, CountDownLatch latch) throws InterruptedException {
                if(i%3==0){
                    latch.await();
                   safeListWrapper.addIfAbsent("a");
                }else if(i%3==1){
                    latch.await();
                    int c= safeListWrapper.findCount("a");
                    if(c>1){
                        System.out.println("error data:"+c);
                    }
                }else{
                    latch.await();
                    safeListWrapper.remove("a");
                }
            }
        });
    }
}
