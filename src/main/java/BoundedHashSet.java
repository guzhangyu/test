import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;

/**
 * Created by guzy on 16/6/25.
 */
public class BoundedHashSet<T> {
    final Set<T> set;
    final Semaphore semaphore;

    public BoundedHashSet(){
        set=new HashSet<T>();
        semaphore=new Semaphore(10);
    }

    public void add(T t){
        if(set.contains(t)){
            return;
        }
        Boolean added=false;
        try {
            semaphore.acquire();
            added=set.add(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
            //Thread.currentThread().interrupt();
        }finally {
            if(!added){
                semaphore.release();
            }
        }
    }

    public void remove(T t){
        if(set.remove(t)){
            semaphore.release();
        }
    }


    public static void main(String[] args) {
        BoundedHashSet set=new BoundedHashSet();
        for(int i=0;i<=10;i++){
            set.add(i);
            System.out.println(i);
        }
    }
}
