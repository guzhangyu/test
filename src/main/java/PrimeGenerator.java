import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by guzy on 16/6/27.
 */
public class PrimeGenerator implements Runnable {

    private final List<BigInteger> primes=new ArrayList<BigInteger>();

    private volatile boolean cancelled;

    public void run(){
        BigInteger p=BigInteger.ONE;
        while(!cancelled){
            primes.add(p.nextProbablePrime());
        }
    }

    public void cancel(){
        cancelled=true;
    }

    public synchronized List<BigInteger> get(){
        return new ArrayList<BigInteger>(primes);
    }

    public static void main(String[] args) {
        PrimeGenerator pg=new PrimeGenerator();
//        ExecutorService es= Executors.newSingleThreadExecutor();
//        es.execute(pg);
        new Thread(pg).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            pg.cancel();
        }
    }
}
