import java.math.BigInteger;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by guzy on 16/6/27.
 */
public class PrimeConsumer extends Thread {

    BlockingQueue<BigInteger> primes;

    volatile Boolean cancelled=false;

    public PrimeConsumer(BlockingQueue<BigInteger> primes){
        this.primes=primes;
    }

    public void run(){
        while(!cancelled){
            try {
                System.out.println(primes.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public static void main(String[] args) {
        BlockingQueue<BigInteger> primes=new ArrayBlockingQueue<BigInteger>(10);
        PrimeProducer producer=new PrimeProducer(primes);
        PrimeConsumer consumer=new PrimeConsumer(primes);
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
        consumer.interrupt();
        producer.interrupt();
    }
}
