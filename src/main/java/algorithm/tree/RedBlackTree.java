package algorithm.tree;

/**
 * 红黑树
 * Created by guzy on 18/2/8.
 */
public class RedBlackTree<T extends Comparable> extends BinarySearchTree<T>{

    public TreeNode<T> insert(TreeNode<T> tree,T v){
        TreeNode<T> newNode=insert(tree,v);
        newNode.setRed(false);
        TreeNode<T> cur=newNode.getPre();
        cur.setRed(true);

        adjust(cur);
        return newNode;
    }

    /**
     * 更换为至多单儿子的节点，并进行移除
     * @param node
     */
    public void changeAndRemove(TreeNode<T> node){
        if(true){//如果节点至多只有一个儿子
            node = replaceToRemoveNode(node);
            //处理叶子节点
        }else{

        }
    }

    public void remove(TreeNode<T> tree,T v){
        TreeNode<T> toRemove=find(tree,v);
        if(toRemove==null){
            return;
        }
        //TreeNode<T> pre=toRemove.getPre();
        if(toRemove.isLeaf()){ // && (pre==null || pre.getLeft()==null || pre.getRight()==null)
            replace(toRemove, null);
            return;
        }

        if(toRemove.isRed()){//肯定不是根
            if(toRemove.getLeft()==null){
                replace(toRemove,toRemove.getRight());
                return;
            }
            if(toRemove.getRight()==null){
                replace(toRemove,toRemove.getLeft());
                //toRemove.getLeft().setRight(toRemove.getRight());
                return;
            }

            if(toRemove.getLeft().isLeaf()){
                TreeNode<T> child=toRemove.getLeft();
                child.setRed(true);
                replace(toRemove,child);
                child.setRight(toRemove.getRight());
                return;
            }
            if(toRemove.getRight().isLeaf()){
                TreeNode<T> child=toRemove.getRight();
                child.setRed(true);
                replace(toRemove,child);
                child.setLeft(toRemove.getLeft());
                return;
            }
            toRemove = replaceToRemoveNode(toRemove);


        }else{//如果要移除的节点为黑色
            //假设下面只有一枝
            if(toRemove.getLeft()==null){
                if(toRemove.getRight().isRed()){
                    toRemove.getRight().setRed(false);
                    replace(toRemove,toRemove.getRight());
                    return;
                }

                TreeNode<T> newNode=toRemove.getRight();
                replace(toRemove,toRemove.getRight());
                if(toRemove.getPre()==null){//如果toRemove为根，结束
                    return;
                }

                TreeNode<T> right=toRemove.getPre().getRight();
                if(right.isRed()){
                    toRemove.getPre().setRight(right.getLeft());
                    right.setLeft(toRemove.getPre());
                    replace(toRemove.getPre(),right);
                }
            }
            if(toRemove.getRight()==null){
                if(toRemove.getLeft().isRed()){
                    toRemove.getLeft().setRed(false);
                    replace(toRemove,toRemove.getLeft());
                    return;
                }
            }
        }

        //如果不是有两个非叶子节点的儿子的情况
        if(toRemove.getLeft()==null || toRemove.getLeft().isLeaf()){
            TreeNode<T> pre=toRemove.getPre();
            if(pre!=null){
                if(pre.getLeft()==toRemove){
                    //pre.setLeft()
                }
            }
        }

    }



    private void backAndChangeRed(TreeNode<T> node){
        TreeNode<T> son=null;
        while(!node.isRed() && node.getPre()!=null){
            son=node;
            node=node.getPre();
        }
        if(node.getLeft()==null || node.getRight()==null){
            node.setRed(false);
        }else if(son==node.getLeft()){
            //node.setRight()
        }
    }

    /**
     * 更换需要删除的节点
     * @param toRemove
     * @return
     */
    private TreeNode<T> replaceToRemoveNode(TreeNode<T> toRemove) {

        TreeNode<T> toReplace=toRemove;
        //转化为单非叶子节点儿子的情况
        while(toReplace.getLeft()!=null || toReplace.getRight()!=null){
            while(toReplace.getLeft()!=null){
                toReplace=toReplace.getLeft();
            }
            while(toReplace.getRight()!=null){
                toReplace=toReplace.getRight();
            }
        }

        T t=toReplace.getValue();
        toReplace.setValue(toRemove.getValue());
        toRemove.setValue(t);
        return toReplace;
    }


    /**
     * 调节红黑
     * @param cur
     */
    private void adjust(TreeNode<T> cur) {
        TreeNode<T> father=cur.getPre();
        if(father!=null){
            if(father.isRed()){//父节点为红色,必有祖父节点
                TreeNode<T> grandFather=father.getPre();

                if(father==grandFather.getLeft()){//父节点为祖父节点的左节点
                    TreeNode<T> uncle=grandFather.getRight();
                    if(uncle==null || !uncle.isRed()){//叔父节点缺失或者是黑色
                        if(father.getLeft()==cur){ //当前节点是父节点的左节点
                            ub_ll(father, grandFather);
                        }else{
                            //父节点逆旋
                            father.setRight(cur.getLeft());
                            cur.setRight(father);

                            ub_ll(cur,grandFather);
                        }
                    }else{
                        ur(father, grandFather, uncle);
                    }
                }else{
                    TreeNode<T> uncle=grandFather.getLeft();
                    if(uncle==null || !uncle.isRed()){
                        if(father.getRight()==cur){
                            ub_rr(father,grandFather);
                        }else{
                            father.setLeft(cur.getRight());
                            cur.setLeft(father);

                            ub_rr(father,grandFather);
                        }
                    }else{
                        ur(father, grandFather, uncle);
                    }
                }

            }
        }
    }

    /**
     * uncle red
     * @param father
     * @param grandFather
     * @param uncle
     */
    private void ur(TreeNode<T> father, TreeNode<T> grandFather, TreeNode<T> uncle) {
        father.setRed(false);
        uncle.setRed(false);
        grandFather.setRed(true);

        TreeNode<T> newFather=grandFather.getPre();
        if(newFather!=null && newFather.isRed()){
            adjust(grandFather);
        }
    }

    /**
     * uncle black left left
     * @param father
     * @param grandFather
     */
    private void ub_ll(TreeNode<T> father, TreeNode<T> grandFather) {
        father.setRed(false);
        grandFather.setRed(true);

        //祖父节点顺旋
        grandFather.setLeft(father.getRight());
        father.setRight(grandFather);
    }

    private void ub_rr(TreeNode<T> father, TreeNode<T> grandFather) {
        father.setRed(false);
        grandFather.setRed(true);

        //祖父节点顺旋
        grandFather.setRight(father.getLeft());
        father.setLeft(grandFather);
    }

}
