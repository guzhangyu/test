import java.util.HashSet;
import java.util.Set;

/**
 * Created by guzy on 16/6/22.
 */
//@ThreadSafe
public class PersonSet {

    private final Set<Person> mySet =new HashSet<Person>();

    public synchronized void addPerson(Person p){
        mySet.add(p);
    }

    public synchronized boolean containsPerson(Person p){
        return mySet.contains(p);
    }
}
