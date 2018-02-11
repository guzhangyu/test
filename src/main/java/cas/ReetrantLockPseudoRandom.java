package cas;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 通过锁来实现seed在使用过程中没有被改变
 * Created by guzy on 16/7/26.
 */
public class ReetrantLockPseudoRandom extends PseudoRandom {

    Lock lock=new ReentrantLock();

    int seed;

    public ReetrantLockPseudoRandom(int seed){
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
        System.out.println(new ReetrantLockPseudoRandom(-4).nextInt(-3));
    }
}
