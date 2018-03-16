package algorithm.tree.b;

import algorithm.tree.Tree;

import java.util.ArrayList;
import java.util.List;

/**
 * b-树
 * @param <K>
 */
public class BMinusTree<K extends Comparable> implements Tree<BNode,K> {

    private BNode<K> root;

    public BMinusTree(int m){
        this.m=m;
    }

    /**
     * 非叶子节点最多儿子数
     */
    private int m;

    public BNode<K> insert(K toInsertK){
        if(root==null){
            BNode<K> root=new BNode<K>();
            root.addKey(toInsertK);
            this.root=root;
        }

        //向下查找，直到直接填充元素，或者是叶子节点
        BNode<K> cur=root;
        while(cur!=null && !cur.isLeaf()){
            //向下进行查找
            if(toInsertK.compareTo(cur.getKeys().get(cur.getKeys().size()-1))>=0){
                cur = getOrAddBNode(cur, cur.getKeys().size(), toInsertK);//如果是新增的元素，直接为null，跳出
            }else if(toInsertK.compareTo(cur.getKeys().get(0))<=0){
                cur = getOrAddBNode(cur, 0, toInsertK);
            }else{
                for(int i=0;i<cur.getKeys().size()-1;i++){
                    if(toInsertK.compareTo(cur.getKeys().get(i))>0 && toInsertK.compareTo(cur.getKeys().get(i+1))<=0){
                        cur = getOrAddBNode(cur, i+1, toInsertK);
                        break;
                    }
                }
            }
        }

        if(cur!=null) {//叶子节点
            cur.addKey(toInsertK);
            fillBNodeUp(cur);
            return cur;
        }

        return null;//dead code
    }

    @Override
    public void remove(K k) {
        BNode<K> node=find(k);
        if(node!=null){
            int index=node.getKeys().indexOf(k);
            node.getKeys().remove(k);
            if(node.getPoints().size()>index && node.getPoints().get(index)!=null){
                //node.getPoints().set(index,null);
                //node.getPoints()
            }
            if(!node.isLeaf()){

            }


            if(node.getKeys().size()==0){
                if(node.getParent()!=null){
                    node.getParent().removePoint(node);
                }else{
                    this.root=null;
                }
            }
        }
    }

    /**
     * 向上填充
     * @param cur
     */
    private void fillBNodeUp(BNode<K> cur) {
        if(cur.getKeys().size()<=m){
            return;
        }

        K upKey=cur.getKeys().remove(cur.getKeys().size()-1);
        while(upKey!=null){
            if(cur.getParent()==null){
                BNode<K> root=new BNode<K>();
                root.addKey(upKey);
                root.addPoint(cur);
                this.root=root;
                return;
            }
            upKey= fillBNodeDown(cur.getParent(),upKey,true);

            cur=cur.getParent();
        }
    }

    /**
     * 填充节点 左右下节点
     * @param curNode
     * @param addedKey
     * @param left 是否向左
     * @return 要移除的key
     */
    private K fillBNodeDown(BNode<K> curNode, K addedKey, Boolean left) {
        curNode.addKey(addedKey);
        int index=curNode.getKeys().indexOf(addedKey);

        if(curNode.getKeys().size()<=m){
            if(!curNode.isLeaf()) {//非叶子节点
                curNode.addPoint(index,null);
            }
            return null;
        }else{
            for(int i=0;i<=m;i++){
                if(i==index){
                    continue;
                }
                if(curNode.getPoint(i)==null){
                    BNode<K> newNode=new BNode<K>();
                    addedKey=curNode.getKeys().remove(i);
                    newNode.getKeys().add(addedKey);
                    curNode.setPoint(i,newNode);//因为原先就有
                    return null;
                }else {//一次向后替换 或 向前替换
                    K newK= fillBNodeDown(curNode.getPoint(i),curNode.getKeys().remove(i),i<index);//往前(后)一个pointer
                    if(newK==null){
                        return null;
                    }
                    curNode.addKey(newK);
                }
            }
        }
        return curNode.getKeys().remove(left?0:curNode.getKeys().size()-1);
    }

    private BNode<K> getOrAddBNode(BNode<K> cur, int index, K toInsertK) {
        BNode<K> tempNode = cur.getPoint(index);
        if(tempNode==null){
            //如果是关键字，没有对应的节点
            BNode<K> newNode=new BNode<K>();
            newNode.getKeys().add(toInsertK);
            cur.setPoint(index,newNode);
        }
        return tempNode;
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

            if(cur.isLeaf()){
                return null;
            }

            //非叶子节点
            if(toFindK.compareTo(keys.get(keys.size()-1))>0){
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
        BMinusTree<Integer> tree=new BMinusTree<Integer>(2);
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

//        tree.initFromArray(new Integer[][][]{
//                {{8,11}},
//                {{8},{9,10},{16,17}},
//                {{},{5,6},{} ,{},{},{11,12} ,{13,14},{}}
//                },2);

        tree.insert(4);
        tree.insert(15);
        tree.insert(11);
        tree.insert(6);
        tree.insert(8);
        System.out.println(tree.find(11));
        tree.remove(6);
        System.out.println(tree.find(6));
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
}
