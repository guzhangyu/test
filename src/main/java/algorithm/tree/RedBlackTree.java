package algorithm.tree;

/**
 * 红黑树
 * Created by guzy on 18/2/8.
 */
public class RedBlackTree<T extends Comparable> extends BinarySearchTree<T>{

    @Override
    public TreeNode<T> insert(T v){
        TreeNode<T> newNode=super.insert(v);
        newNode.setRed(false);
        TreeNode<T> cur=newNode.getPre();

        if(cur!=null && cur.getPre()!=null && (cur.getLeft()==null || cur.getRight()==null) ){//如果父节点不为根节点
            cur.setRed(true);

            adjust4Insert(cur);
        }

        return newNode;
    }

    /**
     * 调节红黑
     * @param cur
     */
    private void adjust4Insert(TreeNode<T> cur) {
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
                            rightDown(father,cur);

                            ub_rr(cur,grandFather);
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
        if(grandFather.getPre()!=null){
            grandFather.setRed(true);
        }

        TreeNode<T> newFather=grandFather.getPre();
        if(newFather!=null && newFather.isRed()){
            adjust4Insert(grandFather);
        }
    }

    /**
     * uncle black left left
     * @param father
     * @param grandFather
     */
    private void ub_ll(TreeNode<T> father, TreeNode<T> grandFather) {
        father.setRed(false);
//        if(grandFather.getPre()!=null){
            grandFather.setRed(true);
//        }

        //祖父节点顺旋
        rightDown(grandFather,father);
    }

    private void ub_rr(TreeNode<T> father, TreeNode<T> grandFather) {
        father.setRed(false);
//        if(grandFather.getPre()!=null){
            grandFather.setRed(true);
//        }

        //祖父节点顺旋
        leftDown(grandFather,father);
    }


    @Override
    public void remove(T v){
        TreeNode<T> toRemove=find(v);
        if(toRemove==null){//找不到，直接返回
            return ;
        }

        //转为至多只有一个儿子的节点
        if(toRemove.getLeft()!=null && toRemove.getRight()!=null){
            toRemove=replaceToRemoveNode(toRemove);
        }

        if(toRemove.getPre()==null){//此时直接返回即可
            replaceRoot(toRemove);
            return;
        }


        TreeNode<T> newNode=toRemove.getLeft()==null?toRemove.getRight():toRemove.getLeft();
        replace(toRemove,newNode);
        if(newNode==null){
            newNode=toRemove.getPre();//肯定不为空
            boolean isRed=newNode.isRed();
            if(newNode.isLeaf()){
                newNode.setRed(false);
            }

            if(isRed){
                return;
            }//否则，继续处理
        }

        if(toRemove.isRed()){//肯定不是根
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
                brother.getRight().setRed(false);
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
        TreePrintTool<Integer> treePrinter=new TreePrintTool<Integer>();

        RedBlackTree<Integer> tree1=new RedBlackTree<Integer>();
        tree1.insert(3);


        RedBlackTree<Integer> tree=new RedBlackTree<Integer>();

        tree.insert(1);
       // treePrinter.printTree(tree.tree);
        tree.insert(4);
        treePrinter.printTree(tree.tree);
        tree.insert(3);
       //
        tree.insert(7);
        treePrinter.printTree(tree.tree);
        tree.insert(6);
        treePrinter.printTree(tree.tree);
        tree.insert(5);
        treePrinter.printTree(tree.tree);
        tree.insert(12);
        treePrinter.printTree(tree.tree);
//        treePrinter.printTree(tree.tree);
//        tree.insert(44);
//        treePrinter.printTree(tree.tree);
//        tree.insert(43);
//        treePrinter.printTree(tree.tree);
//        tree.insert(8);
//        treePrinter.printTree(tree.tree);
//        tree.insert(9);
//        tree.insert(10);
//        tree.insert(11);
        tree.insert(42);

        treePrinter.printTree(tree.tree);

        tree.remove(42);
        treePrinter.printTree(tree.tree);

        tree.remove(4);
        treePrinter.printTree(tree.tree);

        tree.remove(6);
        treePrinter.printTree(tree.tree);

        tree.remove(5);
        treePrinter.printTree(tree.tree);


    }

}
