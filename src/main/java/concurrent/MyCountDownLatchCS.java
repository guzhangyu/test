package concurrent;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class MyCountDownLatchCS {

    Sync sync;

    class Sync extends AbstractQueuedSynchronizer{

        public Sync(int counts){
            this.setState(counts);
        }

        public void decState(){
            int s=getState();
            if(s==0){
                return;
            }
            releaseShared(1);
        }

        public void acquireState() throws InterruptedException {
            if(getState()==0){
                return;
            }
            acquireSharedInterruptibly(1);
        }

        protected int tryAcquireShared(int arg){
            int state=getState();
            if(state==0){
                return 1;
            }
            return -1;
        }

        protected boolean tryReleaseShared(int arg){
            int s=getState();
            while(s>0){
                if(super.compareAndSetState(s,s-1)){
                    if(s==1){
                        return true;
                    }
                    return false;
                }
                s=getState();
            }
            return false;
        }
    }

    public MyCountDownLatchCS(int counts) {
        sync=new Sync(counts);
    }

    public void countDown(){
        sync.decState();
    }

    public void await() throws InterruptedException {
       sync.acquireState();
    }

    public static void main(String[] args) throws InterruptedException {
        final MyCountDownLatchCS countDownLatch=new MyCountDownLatchCS(3);
        countDownLatch.countDown();
        countDownLatch.countDown();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //countDownLatch.await();
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("hhh ");
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("hhh ");
            }
        }).start();

        countDownLatch.await();
        System.out.println("hha ");

    }
}
