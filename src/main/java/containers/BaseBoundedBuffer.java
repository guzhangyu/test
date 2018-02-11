package containers;

/**
 * Created by guzy on 16/7/23.
 */
public class BaseBoundedBuffer<V> {

    private final V[] buf;
    private int tail;
    private int head;
    private int count;

    public BaseBoundedBuffer(int capacity){
        this.buf=(V[])new Object[capacity];
    }

    public synchronized void doPut(V v){
        buf[tail]=v;
        tail++;
        if(tail==buf.length){
            tail=0;
        }
        count++;
    }

    public synchronized V doTake(){
        V v=buf[head];
        buf[head]=null;
        head++;
        if(head==buf.length){
            head=0;
        }
        count--;
        return v;
    }

    public synchronized final boolean isFull(){
        return count==buf.length;
    }

    public synchronized final boolean isEmpty(){
        return count==0;
    }




}
