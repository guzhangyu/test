package concurrent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 加上读写锁控制的map
 * Created by guzy on 16/7/23.
 */
public class ReadWriteMap<K,V> {
    private final Map<K,V> map;

    private final ReadWriteLock lock=new ReentrantReadWriteLock();

    private final Lock r=lock.readLock();

    private final Lock w=lock.writeLock();

    public ReadWriteMap(Map<K,V> map){
        this.map=map;
    }

    public V put(K k,V v){
        w.lock();
        try{
            return map.put(k,v);
        }finally {
            w.unlock();
        }
    }

    public V remove(K k){
        w.lock();
        try{
            return map.remove(k);
        }finally {
            w.unlock();
        }
    }

    public void putAll(Map<K,V> map){
        w.lock();
        try{
            this.map.putAll(map);
        }finally {
            w.unlock();
        }
    }

    public void clear(){
        w.lock();
        try{
            map.clear();
        }finally {
            w.unlock();
        }
    }

    public V get(K k){
        r.lock();
        try{
            return map.get(k);
        }finally {
           // r.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final ReadWriteMap<Integer,Integer> map=new ReadWriteMap<Integer, Integer>(new HashMap<Integer, Integer>());
        new Thread(new Runnable() {
            @Override
            public void run() {
                ;
                System.out.println(map.get(4));
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(map.get(4));
            }
        }).start();


        Thread.sleep(1000l);

        map.put(3,5);
        System.out.println(map.get(3));
    }
}
