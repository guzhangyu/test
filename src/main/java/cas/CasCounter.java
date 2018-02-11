package cas;

/**
 * Created by guzy on 16/7/26.
 */
public class CasCounter {

    private SimulatedCAS value;

    public int getValue(){
        return value.get();
    }

    public int increment(){
        int v=getValue();
        while(v!=value.compareAndSwap(v,v+1));
        return v+1;
    }
}
