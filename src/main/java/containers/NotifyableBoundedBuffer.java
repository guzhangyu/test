package containers;

/**
 * Created by guzy on 16/7/23.
 */
public class NotifyableBoundedBuffer<V> extends BaseBoundedBuffer<V> {
    public NotifyableBoundedBuffer(int capacity) {
        super(capacity);
    }

    public synchronized void put(V v) throws InterruptedException {
        while(isFull()){
            wait();
        }
        doPut(v);
        notifyAll();
    }

    public synchronized V take() throws InterruptedException {
        while(isEmpty()){
            wait();
        }
        V v=doTake();
        notifyAll();
        return v;
    }

    public static void main(String[] args) throws InterruptedException {
        NotifyableBoundedBuffer<Integer> buffer=new NotifyableBoundedBuffer(3);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(buffer.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();;
        buffer.put(4);
    }
}
