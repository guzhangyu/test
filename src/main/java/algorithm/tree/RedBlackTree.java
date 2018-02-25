package algorithm.tree;

/**
 * 红黑树
 * Created by guzy on 18/2/8.
 */
public class RedBlackTree<T extends Comparable> extends BinarySearchTree<T>{

    public TreeNode<T> insert(TreeNode<T> tree,T v){
        TreeNode<T> newNode=super.insert(tree, v);
        newNode.setRed(false);
        TreeNode<T> cur=newNode.getPre();

        if(cur.getPre()!=null){//如果父节点不为根节点
            cur.setRed(true);
            adjust(cur);
        }

        return newNode;
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
//                            father.setRight(cur.getLeft());
//                            cur.setRight(father);
                            leftDown(father,cur);

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
//                            father.setLeft(cur.getRight());
//                            cur.setLeft(father);
                            rightDown(father,cur);

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
        rightDown(grandFather,father);
//        grandFather.setLeft(father.getRight());
//        father.setRight(grandFather);
    }

    private void ub_rr(TreeNode<T> father, TreeNode<T> grandFather) {
        father.setRed(false);
        grandFather.setRed(true);

        //祖父节点顺旋
        leftDown(grandFather,father);
//        grandFather.setRight(father.getLeft());
//        father.setLeft(grandFather);
    }


    public void remove(TreeNode<T> tree,T v){
        TreeNode<T> toRemove=find(tree,v);
        if(toRemove==null){//找不到，直接返回
            return ;
        }

        //转为至多只有一个儿子的节点
        if(toRemove.getLeft()!=null && toRemove.getRight()!=null){
            toRemove=replaceToRemoveNode(toRemove);
        }

        if(toRemove.getPre()==null){//此时直接返回即可
            tree.setValue(toRemove.getValue());
            tree.setColor(toRemove.getColor());
            tree.setLeft(toRemove.getLeft());
            tree.setRight(toRemove.getRight());
            return;
        }

        TreeNode<T> newNode=toRemove.getLeft()==null?toRemove.getRight():toRemove.getLeft();
        replace(toRemove,newNode);
        if(newNode==null){
            newNode=toRemove.getPre();//肯定不为空
        }

        if(newNode.isRed()){//肯定不是根
            return ;
        }
        //如果要移除的节点为黑色
        adjust4Remove(newNode);
    }

    /**
     * 为移除做节点调整
     * @param toRemove
     */
    private void adjust4Remove(TreeNode<T> toRemove) {
        TreeNode<T> pre = toRemove.getPre();
        if (pre == null) {
            return;
        }
        if (pre.getLeft() == toRemove) {//左子树
            TreeNode<T> brother = pre.getRight();
            if (brother == null) {//兄弟为空
                adjust4Remove(pre);//往上回溯
                return;
            }
            if (brother.isRed()) {//兄弟为红色
                leftDown(toRemove, brother); //左旋，兄弟换为父
                adjust4Remove(brother);
                return;
            }
            if (brother.isLeaf()) { //如果兄弟为叶子
                leftDown(pre,brother);
                return;
            }
            if (brother.getLeft() != null && brother.getLeft().isRed()) { //如果兄弟左儿子为红色，更换颜色并右下旋转
                brother.setRed(true);
                brother.getLeft().setRed(false);
                rightDown(brother, brother.getLeft());
                brother = pre.getRight();
            }
            if (brother.getRight() != null && brother.getRight().isRed()) {//如果兄弟右儿子为红色，更换颜色并左下旋转
                brother.setRed(false);
                int preColor = pre.getColor();
                pre.setRed(false);
                brother.setColor(preColor);
                leftDown(pre, brother);
            }
            return;
        }

        //右子树
        TreeNode<T> brother= pre.getLeft();
        if(brother==null){
            adjust4Remove(pre);
            return;
        }
        if(brother.isRed()){
            rightDown(toRemove,brother);
            adjust4Remove(brother);
            return;
        }
        if(brother.isLeaf()){
            rightDown(pre,brother);
            return;
        }
        if(brother.getRight()!=null && brother.getRight().isRed()){
            brother.setRed(true);
            brother.getRight().setRed(false);
            leftDown(brother,brother.getRight());
            brother= pre.getLeft();
        }
        if(brother.getLeft()!=null && brother.getLeft().isRed()){
            brother.setRed(false);
            int preColor= pre.getColor();
            pre.setRed(false);
            brother.setColor(preColor);
            rightDown(pre,brother);
        }
    }

    /**
     * 右下旋转
     * @param toMove
     * @param left
     */
    private void rightDown(TreeNode<T> toMove, TreeNode<T> left) {
        if(toMove.getPre()!=null){
            replace(toMove,left);
        }
        TreeNode<T> lr=left.getRight();
        left.setRight(toMove);
        toMove.setLeft(lr);
    }

    /**
     * 左下旋转
     * @param toMove
     * @param right
     */
    private void leftDown(TreeNode<T> toMove, TreeNode<T> right) {
        if(toMove.getPre()!=null){
            replace(toMove.getPre(),right);
        }

        TreeNode<T> brotherL=right.getLeft();
        right.setLeft(toMove);
        toMove.setRight(brotherL);
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

        toRemove.setValue(toReplace.getValue());
        return toReplace;
    }

    public static void main(String[] args) {
        TreeNode<Integer> treeNode=new TreeNode<Integer>(1);
        treeNode.setRed(false);

        RedBlackTree<Integer> tree=new RedBlackTree<Integer>();
        tree.insert(treeNode,4);
        tree.insert(treeNode,7);
        tree.insert(treeNode,6);

        TreePrinter<Integer> treePrinter=new TreePrinter<Integer>();
        treePrinter.printTree(treeNode);
    }

}
