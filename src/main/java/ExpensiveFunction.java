import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by guzy on 16/6/25.
 */
public class ExpensiveFunction implements Computable<String,BigInteger> {

    @Override
    public BigInteger compute(String arg) throws InterruptedException {
        //compute for a long time
        return new BigInteger(arg);
    }
}

class Memoizer<A,V> implements Computable<A,V>{
    final Map<A,FutureTask<V>> cache=new HashMap<A, FutureTask<V>>();
    final Computable<A,V> c;

    public Memoizer(Computable<A,V> c){
        this.c=c;
    }

    public V compute(final A arg) throws InterruptedException {
       while(true){
           FutureTask<V> result=cache.get(arg);
           if(result==null){
               FutureTask temp=new FutureTask<V>(new Callable<V>() {
                   @Override
                   public V call() throws Exception {
                       return c.compute(arg);
                   }
               });
               //synchronized (cache){
               result=cache.putIfAbsent(arg,temp);
               if(result==null){
                   result=temp;
                   result.run();
               }
               //}
           }
           try {
               return result.get();
           }catch(CancellationException e){
               cache.remove(arg,result);
           } catch (ExecutionException e) {
               e.printStackTrace();
               throw new RuntimeException(e);
           }
       }
    }
}


interface Computable<A,V>{
    V compute(A arg) throws InterruptedException;
}
