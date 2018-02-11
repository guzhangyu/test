package lock;

/**
 * Created by guzy on 16/7/23.
 */
public class ThreadGate {
    private boolean isOpen;

    private int generation;

    public synchronized void close(){
        isOpen=false;
    }

    public synchronized void setOpen(){
        ++generation;
        isOpen=true;
        notifyAll();
    }

    /**
     * 阻塞并直到:opened-since(generation on entry)
     */
    public synchronized void await(){
        int arrivalGeneration =generation;
        while(!isOpen && arrivalGeneration==generation){
            await();
        }
    }
}
