package algorithm.tree.b;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BNode<K extends Comparable> {

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

    public Boolean isLeaf(){
        if(CollectionUtils.isEmpty(points)){
            return true;
        }
        for(BNode node:points){
            if(node!=null){
                return false;
            }
        }
        points.clear();
        return true;
    }

    public BNode<K> getPoint(int index){
        if(index<points.size()){
            return points.get(index);
        }
        return null;
    }

    public void removePoint(BNode<K> node){
        int index=points.indexOf(node);
        if(index>=0){
            points.set(index,null);

            //如果全为null，则直接清空
            for(BNode n:points){
                if(n!=null){
                    return;
                }
            }
            points.clear();
        }
    }

    public BNode<K> addPoint(BNode<K> point){
        this.points.add(point);
        if(point!=null){
            point.setParent(this);
        }
        return this;
    }

    public BNode<K> addPoint(int index,BNode<K> point){
        if(index>=this.points.size()){
            this.points.add(null);
        }
        this.points.add(index,point);
        if(point!=null){
            point.setParent(this);
        }
        return this;
    }

    public void addKey(K k){
        keys.add(k);
        Collections.sort(keys);
    }

    public BNode<K> setPoint(int index,BNode<K> point){
        if(index<this.points.size()){
            this.points.set(index,point);
        }else{
            this.points.add(null);
            this.points.add(index,point);
        }
        if(point!=null){
            point.setParent(this);
        }
        return this;
    }
}
