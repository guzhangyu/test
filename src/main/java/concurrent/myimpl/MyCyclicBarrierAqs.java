package concurrent.myimpl;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * 根据Aqs实现的栅栏
 */
public class MyCyclicBarrierAqs {

    private final int cnt;

    private final AtomicInteger cntA=new AtomicInteger();

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
            while(true){
                int s=getState();

                boolean success=compareAndSetState(s,s==0?MyCyclicBarrierAqs.this.cnt:s-1);
                if(success){
                    if(s==1){
                        compareAndSetState(0,MyCyclicBarrierAqs.this.cnt);
                        cntA.compareAndSet(0,MyCyclicBarrierAqs.this.cnt);
                        conditionObject.signalAll();
                    }
                    return true;
                }
            }
        }

        protected boolean isHeldExclusively(){
            return true;
        }

        protected boolean tryAcquire(int arg){
            int s;
            while((s=cntA.get())>0){
                if(cntA.compareAndSet(s,s-1)){
                    if(s>1){
                        conditionObject.signalAll();
                    }
                    return true;
                }
            }
            return false;
        }
    }

    public int await() throws InterruptedException{
        return sync.await();
    }
}