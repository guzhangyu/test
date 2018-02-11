package cas;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 基于类的cas
 * Created by guzy on 16/7/26.
 */
public class CasNumberRange {
    private static class IntPair{
        final int lower;
        final int upper;

        public IntPair(int lower, int upper) {
            this.lower = lower;
            this.upper = upper;
        }

        public int getLower() {
            return lower;
        }

        public int getUpper() {
            return upper;
        }

        @Override
        public int hashCode() {
            return (lower+"_"+upper).hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if(obj==null || !(obj instanceof IntPair)){
                return false;
            }
            IntPair intPair=(IntPair)obj;
            return intPair.getUpper()==upper && intPair.getLower()==lower;
        }
    }

    private final AtomicReference<IntPair> intPair=new AtomicReference<IntPair>(new IntPair(0,0));

    public int getLower(){
        return intPair.get().getLower();
    }

    public int getUpper(){
        return intPair.get().getUpper();
    }

    public void setLower(int lower){
        IntPair cur=null;
        do{
            cur=intPair.get();
            if(cur.getUpper()<lower){
                throw new IllegalArgumentException("lower is bigger than upper");
            }
        }while(!intPair.compareAndSet(cur,new IntPair(lower,cur.getUpper())));
    }

    public void setUpper(int upper){
        IntPair cur=null;
        do{
            cur=intPair.get();
            if(cur.getLower()>upper){
                throw new IllegalArgumentException("upper is smaller than lower");
            }
        }while(!intPair.compareAndSet(cur,new IntPair(cur.getLower(),upper)));
    }
}
