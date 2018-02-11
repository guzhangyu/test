import java.math.BigInteger;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by guzy on 16/6/27.
 */
public class BrokenPrimeConsumer extends Thread {

    BlockingQueue<BigInteger> primes;

    volatile Boolean cancelled=false;

    public BrokenPrimeConsumer(BlockingQueue<BigInteger> primes){
        this.primes=primes;
    }

    public void run(){
        while(!cancelled){
            try {
                System.out.println(primes.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        BlockingQueue<BigInteger> primes=new ArrayBlockingQueue<BigInteger>(10);
        BrokenPrimeProducer producer=new BrokenPrimeProducer(primes);
        BrokenPrimeConsumer consumer=new BrokenPrimeConsumer(primes);
        producer.start();
        consumer.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        consumer.cancelled=true;
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        producer.cancelled=true;
    }
}
