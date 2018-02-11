/**
 * Created by guzy on 16/6/22.
 */
public class MutablePoint {

    public int x,y;

    public MutablePoint(){
        x=0;
        y=0;
    }

    public MutablePoint(MutablePoint p){
        this.x=p.x;
        this.y=p.y;
    }
}
