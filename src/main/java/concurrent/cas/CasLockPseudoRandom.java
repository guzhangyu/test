package concurrent.cas;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 数值的cas
 * Created by guzy on 16/7/26.
 */
public class CasLockPseudoRandom extends PseudoRandom {

    private final AtomicInteger seed;

    public CasLockPseudoRandom(int seed){
        this.seed=new AtomicInteger(seed);
    }

    public int nextInt(int n){
        int seed;
        int s;
        int remain;
        do{
            s=this.seed.get();
            seed=calculateNext(s);

            remain=s%n;
            remain=remain>0?remain:remain+n;
        }while(!this.seed.compareAndSet(s,seed));
        return remain;
    }

}
