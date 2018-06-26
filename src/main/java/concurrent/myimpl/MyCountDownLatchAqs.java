package concurrent.myimpl;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * 根据aqs实现的闭锁
 */
public class MyCountDownLatchAqs implements MyCountDownLatch{

    Sync sync;

    class Sync extends AbstractQueuedSynchronizer{

        public Sync(int counts){
            this.setState(counts);
        }

        /**
         * 降低state至0
         */
        public void decreaseState(){
            if(getState()>0) {
                releaseShared(1);
            }
        }

        /**
         * 获得执行权限
         * @throws InterruptedException
         */
        public void acquireState() throws InterruptedException {
            //<=0直接执行
            if(getState()>0) {
                acquireSharedInterruptibly(1);
            }
        }

        protected int tryAcquireShared(int arg){
            return getState()==0?1:-1;
        }

        /**
         * 无论如何，在未减到0前，一定要减1
         * @param arg
         * @return
         */
        protected boolean tryReleaseShared(int arg){
           int s;
            while((s=getState())>0){
                if(compareAndSetState(s,s-1)){
                    return s==1;
                }
            }
            return false;
        }
    }

    public MyCountDownLatchAqs(int counts) {
        sync=new Sync(counts);
    }

    public void countDown(){
        sync.decreaseState();
    }

    public void await() throws InterruptedException {
       sync.acquireState();
    }
}
