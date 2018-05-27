package concurrent.cas;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 通过锁来实现seed在使用过程中没有被改变
 * Created by guzy on 16/7/26.
 */
public class ReentrantLockPseudoRandom extends PseudoRandom {

    Lock lock=new ReentrantLock();

    int seed;

    public ReentrantLockPseudoRandom(int seed){
        this.seed=seed;
    }

    public int nextInt(int n){
        try{
            lock.lock();

            int s=seed;
            seed= calculateNext(seed);
            s%=n;
            s=s>0?s:s+n;
            return s;

        }finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        ReentrantLockPseudoRandom random=new ReentrantLockPseudoRandom(-4);
        random.lock.lock();
        new Thread(new Runnable() {
            @Override
            public void run() {
                random.lock.unlock();
                System.out.println(random.nextInt(-3));
            }
        }).start();
    }
}
