package containers;

import java.util.concurrent.CountDownLatch;

/**
 * 分段锁map
 * Created by guzy on 16/7/16.
 */
public class StripedMap {

    private final int N_LOCKS=16;

    private final Node[] nodes;

    private final Object []locks=new Object[N_LOCKS];

    private volatile int size=0;

    private final int numBuckets;

    class Node{
        public Node next;

        public Object key;

        public Object value;

        public Node(Object key, Object value) {
            this.key = key;
            this.value = value;
        }
    }

    public StripedMap(int numBuckets){
        this.numBuckets=numBuckets;
        nodes=new Node[numBuckets];
        for(int i=0;i<N_LOCKS;i++){
            locks[i]=new Object();
        }
    }

    public int hash(Object o){
        return Math.abs(o.hashCode()%numBuckets);
    }

    /**
     * 根据键值获取元素
     * @param key
     * @return
     */
    public Object get(Object key){
        int hash=hash(key);
        synchronized (locks[hash]){
            for(Node n=nodes[hash];n!=null;n=n.next){
                if(n.key.equals(key)){
                    return n.value;
                }
            }
        }
        return null;
    }

    /**
     * 设置键值对
     * @param key 键
     * @param value 值
     */
    public void set(Object key,Object value){
        int hash=hash(key);
        synchronized (locks[hash%N_LOCKS]){
            Node n=nodes[hash];
            if(n==null){
                nodes[hash]=new Node(key,value);
                size++;
            }else{
                Node pre=n;
                while(n!=null){
                    if(n.key.equals(key)){
                        n.value=value;
                        return;
                    }
                    pre=n;
                    n=n.next;
                }
                pre.next=new Node(key,value);
                size++;
            }
        }
    }

    /**
     * 根据键值移除元素
     * @param key
     * @return
     */
    public Object remove(Object key){
        int hash=hash(key);
        synchronized (locks[hash%N_LOCKS]){
            Node n=nodes[hash];
            while(n!=null){
                if(n.key.equals(key)){
                    return n.value;
                }
                n=n.next;
            }
        }
        return null;
    }

    public void clear(){
        for(int i=0;i<numBuckets;i++){
            synchronized (locks[i%N_LOCKS]){
                nodes[i]=null;
            }
        }
        size=0;
    }

    public int size(){
//        int size=0;
//        for(int i=0;i<N_LOCKS;i++){
//            Node n=nodes[i];
//            while(n!=null){
//                size++;
//                n=n.next;
//            }
//        }
        return size;
    }

    public static void main(String[] args) {
        final StripedMap map=new StripedMap(10);
        final CountDownLatch latch=new CountDownLatch(1);
        for(int i=0;i<10;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (map){
                        map.set(1,1);
                        map.set(2,1);
                        map.set(1,3);
                        System.out.println(String.format("curThread:%s,after set:%d",Thread.currentThread().getId(),map.size()));
                        map.clear();
                        System.out.println(String.format("curThread:%s,after clear:%d", Thread.currentThread().getId(), map.size()));
                    }

                }
            }).start();
        }
        latch.countDown();
    }
}
