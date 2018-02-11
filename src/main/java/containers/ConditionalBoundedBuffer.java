package containers;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by guzy on 16/7/23.
 */
public class ConditionalBoundedBuffer<V> {
    private final V[] buf;

    private int head=0;

    private int tail=0;

    private int count=0;

    private final Lock lock=new ReentrantLock();

    private final Condition notFull=lock.newCondition();

    private final Condition notEmpty=lock.newCondition();

    public ConditionalBoundedBuffer(int capacity){
        buf=(V[])new Object[capacity];
    }

    public V take(){
        try{
            lock.lock();
            while(count==0){
                notEmpty.await();
            }

            count--;
            if(count==buf.length-1)
            notFull.signal();

            V v=buf[tail];
            buf[tail]=null;
            if(++tail==buf.length){
                tail=0;
            }
            return v;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return take();
        }finally {
            lock.unlock();
        }
    }

    public void put(V v){
        try{
            lock.lock();
            while(count==buf.length){
                notFull.await();
            }

            count++;
            if(count==1)
            notEmpty.signal();

            buf[head]=v;
            if(++head==buf.length){
                head=0;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            put(v);
        }finally {
            lock.unlock();
        }
    }
}
