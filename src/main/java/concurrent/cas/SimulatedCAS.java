package concurrent.cas;

/**
 * 自己实现的cas
 * Created by guzy on 16/7/26.
 */
public class SimulatedCAS {

    private int value;

    public synchronized int get(){
        return value;
    }

    public synchronized int compareAndSwap(int beforeValue,int newValue){
        if(beforeValue==value){
            value= newValue;
            return beforeValue;
        }else{
            return value;
        }
       // new LinkedTransferQueue().
    }

    public synchronized boolean cas(int beforeValue,int newValue){
        return compareAndSwap(beforeValue,newValue)==beforeValue;
    }
}
