package containers;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by guzy on 16/7/23.
 */
public class BlockedBoundedBuffer<V> extends BaseBoundedBuffer<V> {
    Lock lock=new ReentrantLock();

    Condition notFullCon=lock.newCondition();

    Condition notEmptyCon=lock.newCondition();

    public BlockedBoundedBuffer(int capacity) {
        super(capacity);
    }

    public void put(V v){
        lock.lock();
        try {
            synchronized (notFullCon){
                while(isFull()){
                    notFullCon.await();
                }
                putAndSig(v);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            put(v);
        }finally {
            lock.unlock();
        }
    }

    void putAndSig(V v) {
        Boolean isEmpty=isEmpty();
        doPut(v);
        if(isEmpty)
        notEmptyCon.signal();
        return;
    }

    public V take(){
        lock.lock();
        try{
           synchronized (notEmptyCon){
               while(isEmpty()){
                   notEmptyCon.await();
               }
               return takeAndSig();
           }
        }catch (InterruptedException e){
            e.printStackTrace();
            return take();
        }finally {
            lock.unlock();
        }
    }

    V takeAndSig() {
        Boolean isFull=isFull();
        V v= doTake();
        if(isFull)
        notFullCon.signal();
        return v;
    }

    public static void main(String[] args) {
        BlockedBoundedBuffer<Integer> buffer=new BlockedBoundedBuffer<Integer>(10);
        new Thread(new Runnable() {
            @Override
            public void run() {
                buffer.take();
            }
        }).start();
        buffer.put(3);

    }
}
