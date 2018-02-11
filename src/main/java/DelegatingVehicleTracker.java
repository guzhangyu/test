import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by guzy on 16/6/22.
 */
public class DelegatingVehicleTracker {

    private final ConcurrentHashMap<String,Point>  locations;

    private final Map<String,Point> unmodifiableLocations;

    public DelegatingVehicleTracker(Map<String,Point> points){
        this.locations=new ConcurrentHashMap<String, Point>(points);
        this.unmodifiableLocations= Collections.unmodifiableMap(this.locations);
    }

    public Map<String,Point> getLocations(){
        return unmodifiableLocations;
    }

    public Point getLocation(String id){
        return unmodifiableLocations.get(id);
    }

    public void setLocation(String id,Point point){
        if(locations.replace(id,point)==null)
            throw new IllegalArgumentException("No such ID:"+id);
    }
}
