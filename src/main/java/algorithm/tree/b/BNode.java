package algorithm.tree.b;

import java.util.ArrayList;
import java.util.List;

public class BNode<K> {

    private BNode<K> parent;

    private List<K> keys=new ArrayList<K>();

    private List<BNode> points=new ArrayList<BNode>();

    public BNode<K> getParent() {
        return parent;
    }

    public void setParent(BNode<K> parent) {
        this.parent = parent;
    }

    public List<K> getKeys() {
        return keys;
    }

    public void setKeys(List<K> keys) {
        this.keys = keys;
    }

    public List<BNode> getPoints() {
        return points;
    }

    public void setPoints(List<BNode> points) {
        this.points = points;
    }

    public BNode<K> addPoint(BNode<K> point){
        this.points.add(point);
        if(point!=null){
            point.setParent(this);
        }
        return this;
    }
}
