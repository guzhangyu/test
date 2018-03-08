package algorithm.tree.b;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * b-树
 * @param <K>
 */
public class BMinusTree<K extends Comparable> {

    private BNode<K> root;

    /**
     * 非叶子节点最多儿子数
     */
    private int m;

    public void insert(K toInsertK){
        BNode<K> cur=root;
        while(cur!=null){
            if(CollectionUtils.isEmpty(cur.getPoints())) {//叶子节点
                cur.addKey(toInsertK);
                if(cur.getKeys().size()<=m){
                    return;
                }

                K toUp=cur.getKeys().remove(cur.getKeys().size()-1);
                while(true){
                    BNode<K> parent=cur.getParent();
                    if(parent==null){
                        BNode<K> root=new BNode<K>();
                        root.addKey(toUp);
                        root.addPoint(cur);
                        this.root=root;
                        return;
                    }
                    toUp=fillBNodeWithKey(toUp, parent);
                    if (toUp==null) return;
//                    else if(parent.getPoints().size()<m+1){
//                        BNode<K> newNode=new BNode<K>();
//                        toUp=parent.getKeys().remove(parent.getPoints().size()-1);
//                        newNode.getKeys().add(toUp);
//                        parent.addPoint(newNode);
//                    }else{
//                        for(int i=0;i<parent.getPoints().size();i++){
//                            if(parent.getPoints().get(i)==null){
//                                BNode<K> newNode=new BNode<K>();
//                                toUp=parent.getKeys().remove(i);
//                                newNode.getKeys().add(toUp);
//                                parent.setPoint(i,newNode);
//                                break;
//                            }
//                        }
//                    }

                    cur=parent;
                }
            }

            if(toInsertK.compareTo(cur.getKeys().get(cur.getKeys().size()-1))>=0){
                cur = getOrAddBNode(toInsertK, cur, cur.getKeys().size());//如果是新增的元素，直接为null，跳出
            }else if(toInsertK.compareTo(cur.getKeys().get(0))<=0){
                cur = getOrAddBNode(toInsertK, cur, 0);
            }else{
                for(int i=0;i<cur.getKeys().size()-1;i++){
                    if(toInsertK.compareTo(cur.getKeys().get(i))>0 && toInsertK.compareTo(cur.getKeys().get(i+1))<=0){
                        cur = getOrAddBNode(toInsertK, cur, i+1);
                        break;
                    }
                }
            }
        }

        return ;
    }

    /**
     * 填充节点,右下方向
     * @param addedKey
     * @param curNode
     * @return 多出来的key
     */
    private K fillBNodeWithKey(K addedKey, BNode<K> curNode) {
        curNode.addKey(addedKey);
        int index=curNode.getKeys().indexOf(addedKey);

        if(curNode.getKeys().size()<=m){
            if(!CollectionUtils.isEmpty(curNode.getPoints())) {//叶子节点
                curNode.addPoint(index,null);
            }
            return null;
        }else if(index<m){
            for(int i=index+1;i<=m+1;i++){
                if(curNode.getPoint(i)==null){
                    BNode<K> newNode=new BNode<K>();
                    addedKey=curNode.getKeys().remove(i);
                    newNode.getKeys().add(addedKey);
                    curNode.setPoint(i,newNode);//因为原先就有
                    return null;
                }else {
                    BNode<K> tempNode=curNode.getPoint(i);//往前一个pointer
                    K tempK=curNode.getKeys().remove(i);
                    K newK=fillBNodeWithKey(tempK,tempNode);
                    if(newK==null){
                        return null;
                    }
                    curNode.addKey(newK);
                }
            }
        }
        //TODO:对于加到最后一个节点的情况，直接回溯
        return curNode.getKeys().remove(curNode.getKeys().size()-1);
    }

    private BNode<K> getOrAddBNode(K toInsertK, BNode<K> cur, int s) {
        BNode<K> tempNode = cur.getPoint(s);
        if(tempNode==null){
            //如果是关键字，没有对应的节点
            BNode<K> newNode=new BNode<K>();
            newNode.getKeys().add(toInsertK);
            cur.setPoint(s,newNode);
        }
        return tempNode;
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
//                if(keys.size()==2 && cur.getPoints().size()==1){
//                    System.out.println(cur.getKeys());
//                }
                cur=cur.getPoint(keys.size());
            }else if(toFindK.compareTo(keys.get(0))<0){
                cur=cur.getPoint(0);
            }else{
                for(int i=0;i<keys.size()-1;i++){
                    if(toFindK.compareTo(keys.get(i))>0 && toFindK.compareTo(keys.get(i+1))<0){
                        cur=cur.getPoint(i+1);
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
                {{},{5,6},{} ,{},{},{11,12} ,{13,14},{}}
                },2);

        tree.insert(15);
        tree.insert(11);
        System.out.println(tree.find(11));
        System.out.println(tree.find(18));
    }
}
