import java.math.BigInteger;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by guzy on 16/6/27.
 */
public class BrokenPrimeProducer extends Thread {

    BlockingQueue<BigInteger> queue=null;

    volatile Boolean cancelled=false;

    public BrokenPrimeProducer(BlockingQueue<BigInteger> queue){
        this.queue=queue;
    }

    public void run(){
        BigInteger p=BigInteger.ONE;
        while(!cancelled){
            try {
                p=p.nextProbablePrime();
                queue.put(p);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
