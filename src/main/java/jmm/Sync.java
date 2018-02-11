package jmm;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * Created by guzy on 16/7/26.
 */
public class Sync<V> extends AbstractQueuedSynchronizer {
    private static final int RUNNING=1,RAN=2,CANCELLED=4;
    private V result;
    private Exception exception;

    void innerSet(V v){
        while(true){
            int s =getState();
            if(ranOrCancelled(s)){
                return;
            }
            if(compareAndSetState(s,RAN)){//成功之后，在release之前都会锁住
                break;
            }
        }
        result=v;
        releaseShared(0);
        //done();
    }

    private Boolean ranOrCancelled(int s){
        return s==RAN || s==CANCELLED;
    }

    V innerGet() throws InterruptedException,ExecutionException{
        acquireSharedInterruptibly(0);
        if(getState()==CANCELLED){
            throw new CancellationException();
        }
        if(exception!=null){
            throw new ExecutionException(exception);
        }
        return result;
    }

}
