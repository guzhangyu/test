package concurrent.myimpl;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * 根据Aqs实现的栅栏
 */
public class MyCyclicBarrierAqs {

    private final int cnt;

    public MyCyclicBarrierAqs(int cnt){
        this.cnt=cnt;
        sync=new Sync(cnt);
    }

    Sync sync;

    class Sync extends AbstractQueuedSynchronizer {

        public Sync(int counts) {
            this.setState(counts);
        }

        ConditionObject conditionObject=new ConditionObject();

        public int await() throws InterruptedException {
            int result=getState();
            conditionObject.await();
            return result;
        }

        protected boolean tryRelease(int arg){
            int s=getState();

            boolean success=compareAndSetState(s,s==0?MyCyclicBarrierAqs.this.cnt:s-1);
            if(success && s==1){
                conditionObject.signalAll();
                compareAndSetState(0,MyCyclicBarrierAqs.this.cnt);
            }

            return true;
        }
    }

    public int await() throws InterruptedException{
        return sync.await();
    }
}