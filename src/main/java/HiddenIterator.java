import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by guzy on 16/6/25.
 */
public class HiddenIterator {

    private final Set<Integer> set=new HashSet<Integer>();

    public synchronized void add(Integer i){
        set.add(i);
    }

    public synchronized void remove(Integer i){
        set.remove(i);
    }

    public void addTenThings(){
        Random r =new Random();
        for(int i=0;i<10;i++){
            add(i);
        }
        System.out.println(set.toString());
        System.out.println("DEBUG: added ten elements to "+set);
    }

    public static void main(String[] args) {
        new HiddenIterator().addTenThings();
    }
}
