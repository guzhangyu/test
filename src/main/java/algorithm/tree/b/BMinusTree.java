package algorithm.tree.b;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BMinusTree<K extends Comparable> {

    private BNode<K> root;

    /**
     * 非叶子节点最多儿子数
     */
    private int m;

    public void insert(K toInsertK){
        BNode<K> cur=root;
        while(cur!=null){
            List<K> keys=cur.getKeys();
            if(CollectionUtils.isEmpty(cur.getPoints())) {//叶子节点
                keys.add(toInsertK);
                Collections.sort(keys);
                while(keys.size()>m){
                    cur=cur.getParent();
                    keys=cur.getKeys();
                }
                return ;
            }

            if(toInsertK.compareTo(keys.get(keys.size()-1))>0){
                cur=cur.getPoints().get(keys.size());
            }else if(toInsertK.compareTo(keys.get(0))<0){
                cur=cur.getPoints().get(0);
            }else{
                for(int i=0;i<keys.size()-1;i++){
                    if(toInsertK.compareTo(keys.get(i))>0 && toInsertK.compareTo(keys.get(i+1))<0){
                        cur=cur.getPoints().get(i+1);
                        break;
                    }
                }
            }

        }
        return ;
    }

    public void initFromArray(K[][][] keys,int m){
        root=new BNode<K>();

        List<BNode<K>> nodes=new ArrayList<BNode<K>>();
        int ps=(m+1);
        for(int level=0;level<keys.length;level++){
            List<BNode<K>> curNodes=new ArrayList<BNode<K>>();
            for(int i=0;i<keys[level].length && i<(level==0?1:nodes.size()*ps);i++){
                //底下没有对应的儿子
                if(keys[level][i].length==0){
                    BNode<K> curNode=nodes.get(i/ps);
                    curNode.addPoint(null);
                    continue;
                }

                //当前节点赋值
                BNode<K> bNode=new BNode<K>();
                for(int j=0;j<keys[level][i].length;j++){
                    bNode.getKeys().add(keys[level][i][j]);
                }
                curNodes.add(bNode);

                if(level!=0){
                    //设置指针
                    BNode<K> curNode=nodes.get(i/ps);
                    curNode.addPoint(bNode);
                }else{
                    root=bNode;
                }
            }
            nodes=curNodes;
        }
    }

    /**
     * 查找元素
     * @param toFindK 需要寻找的键值
     * @return
     */
    public BNode<K> find(K toFindK){
        BNode<K> cur=root;
        while(cur!=null){
            //如果找到，直接返回
            List<K> keys=cur.getKeys();
            for(K k:keys){
                if(k.equals(toFindK)){
                    return cur;
                }
            }

            if(CollectionUtils.isEmpty(cur.getPoints())){//叶子节点
                return null;
            }

            //非叶子节点
            if(toFindK.compareTo(keys.get(keys.size()-1))>0){
                cur=cur.getPoints().get(keys.size());
            }else if(toFindK.compareTo(keys.get(0))<0){
                cur=cur.getPoints().get(0);
            }else{
                for(int i=0;i<keys.size()-1;i++){
                    if(toFindK.compareTo(keys.get(i))>0 && toFindK.compareTo(keys.get(i+1))<0){
                        cur=cur.getPoints().get(i+1);
                        break;
                    }
                }
            }
        }
        return null;
    }


    public static void main(String[] args) {
        BMinusTree<Integer> tree=new BMinusTree<Integer>();
//        tree.root=new BNode<Integer>();
//        tree.root.getKeys().add(8);
//        tree.root.getKeys().add(11);
//
//
//        BNode<Integer> o1=new BNode<Integer>();
//        tree.root.addPoint(o1);
//        o1.getKeys().add(4);
//        o1.getKeys().add(5);
//
//        BNode<Integer> o2=new BNode<Integer>();
//        tree.root.addPoint(o2);
//        o2.getKeys().add(9);
//        o2.getKeys().add(10);
//
//        BNode<Integer> o3=new BNode<Integer>();
//        tree.root.addPoint(o3);
//        o3.getKeys().add(12);
//        o3.getKeys().add(13);

        tree.initFromArray(new Integer[][][]{
                {{8,11}},
                {{4,8},{9,10},{16,17}},
                {{},{5,6},{} ,{},{},{11,12} ,{13,14}}
                },2);

        System.out.println(tree.find(14));
    }
}
