package algorithm.tree.b;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;


public class BPlusTree<K extends Comparable> {

    BNode<K> root;

    int m;

    public void initFromArray(K[][][] keys,int m){
        root=new BNode<K>();

        List<BNode<K>> nodes=new ArrayList<BNode<K>>();
        int ps=m;
        for(int level=0;level<keys.length;level++){
            List<BNode<K>> curNodes=new ArrayList<BNode<K>>();
            for(int i=0;i<keys[level].length && i<(level==0?1:nodes.size()*ps);i++){
                //底下没有对应的儿子
                if(keys[level][i].length==0){
                    BNode<K> curNode=nodes.get(i/ps);
                    curNode.getPoints().add(null);
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
                    curNode.getPoints().add(bNode);
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
            if(toFindK.compareTo(keys.get(0))<0){
                return null;
            }else{
                int i=0;
                while(i<keys.size() && toFindK.compareTo(keys.get(i))>0 ){
                    i++;
                }

                cur=cur.getPoints().get(i-1);
            }
        }
        return null;
    }

    public static void main(String[] args) {
        BPlusTree<Integer> tree=new BPlusTree<Integer>();
        tree.initFromArray(new Integer[][][]{
                {{0,14}},
                {{0,6},{14,18}},
                {{0,4},{6,8},{14,15},{18,19}}
                },2);

        System.out.println(tree.find(9));
    }
}
