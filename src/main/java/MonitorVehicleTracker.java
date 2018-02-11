import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by guzy on 16/6/22.
 */
public class MonitorVehicleTracker {

    private final Map<String,MutablePoint> locations;

    public MonitorVehicleTracker(Map<String,MutablePoint> locations){
        this.locations=locations;
    }

    public synchronized Map<String,MutablePoint> getLocations(){
        return deepCopy(locations);
    }

    public synchronized MutablePoint getLocation(String id){
        MutablePoint loc= locations.get(id);
        return loc==null ? null:new MutablePoint(loc);
    }

    public synchronized void setLocation(String id,int x,int y){
        MutablePoint loc =locations.get(id);
        if(loc==null)
            throw new IllegalArgumentException("No such ID:"+id);
        loc.x=x;
        loc.y=y;
    }

    private static Map<String, MutablePoint> deepCopy(Map<String, MutablePoint> locations) {
        Map<String,MutablePoint> newLocations=new HashMap<String, MutablePoint>();
        for(Map.Entry<String,MutablePoint> entry:newLocations.entrySet()){
            newLocations.put(entry.getKey(),entry.getValue());
        }
        return Collections.unmodifiableMap(newLocations);
    }
}
