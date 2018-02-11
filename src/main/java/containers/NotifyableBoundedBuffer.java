package containers;

/**
 * Created by guzy on 16/7/23.
 */
public class NotifyableBoundedBuffer<V> extends BaseBoundedBuffer<V> {
    public NotifyableBoundedBuffer(int capacity) {
        super(capacity);
    }

    public void put(V v) throws InterruptedException {
        while(isFull()){
            wait();
        }
        doPut(v);
        notifyAll();
    }

    public V take() throws InterruptedException {
        while(isEmpty()){
            wait();
        }
        V v=doTake();
        notifyAll();
        return v;
    }
}
