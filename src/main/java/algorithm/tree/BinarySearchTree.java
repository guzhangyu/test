package algorithm.tree;

/**
 * 二叉查找树
 * Created by guzy on 18/2/7.
 */
public class BinarySearchTree<T extends Comparable> {

    TreeNode<T> tree;


    /**
     * 查找某个节点
     * @param v
     * @return
     */
    public TreeNode<T> find(T v) {
        return find(tree, v);
    }

    /**
     * 递归查找某个节点
     * @param tree
     * @param v
     * @return
     */
    private TreeNode<T> find(TreeNode<T> tree, T v) {
        if (tree == null) {
            return null;
        }
        if (tree.getValue().equals(v)) {
            return tree;
        }
        if (tree.getValue().compareTo(v) > 0) {
            return find(tree.getLeft(), v);
        } else {
            return find(tree.getRight(), v);
        }
    }

    /**
     * 删除树上的某个节点
     * @param v
     */
    public void remove(T v){
        TreeNode<T> node=find(tree,v);
        if(node==null){
            return;
        }

        if(node.getLeft()==null && node.getRight()==null){//如果为叶子节点
            replace(node,null);
        }else if(node.getLeft()==null){
            replace(node,node.getRight());
        }else if(node.getRight()==null){
            replace(node, node.getLeft());
        }else{//用后继节点替代

            //找到右子树中的最左节点
            TreeNode<T> rightNext=node.getRight();
            while(rightNext.getLeft()!=null){
                rightNext=rightNext.getLeft();
            }

            //将替代节点的右子树挂到父节点的左子树下
            if(rightNext!=node.getRight()){
                rightNext.getPre().setLeft(rightNext.getRight());
            }
            //用后继节点替代
            rightNext.setLeft(node.getLeft());
            replace(node,rightNext);
        }
    }


    public TreeNode<T> insert(T v) {
        TreeNode<T> node=new TreeNode<T>(v);
        if(tree==null){
            tree=node;
        }else{
            TreeNode<T> cur=tree;
            while(needLeft(v, cur) || needRight(v, cur)){
                while(needRight(v, cur)){
                    cur=cur.getRight();
                }
                while(needLeft(v, cur)){
                    cur=cur.getLeft();
                }
            }

            if(v.compareTo(cur.getValue())>0){
                cur.setRight(node);
            }else{
                cur.setLeft(node);
            }
        }

        return node;
    }

    /**
     * 右下旋转
     * @param toMove
     * @param left
     */
    protected void rightDown(TreeNode<T> toMove, TreeNode<T> left) {
        replace(toMove,left);

        TreeNode<T> lr=left.getRight();
        left.setRight(toMove);
        toMove.setLeft(lr);
        if(toMove.isLeaf()){
            toMove.setRed(false);
        }
    }

    /**
     * 左下旋转
     * @param toMove
     * @param right
     */
    protected void leftDown(TreeNode<T> toMove, TreeNode<T> right) {
        replace(toMove,right);

        TreeNode<T> brotherL=right.getLeft();
        right.setLeft(toMove);
        toMove.setRight(brotherL);
        if(toMove.isLeaf()){
            toMove.setRed(false);
        }
    }

    protected void replaceRoot(TreeNode<T> newRoot) {
        tree=newRoot;
        if(tree!=null){
            tree.setPre(null);
            tree.setRed(false);
        }
    }

    /**
     * 根据中序遍历的规则判断
     * @param v
     * @param cur
     * @return
     */
    protected boolean needLeft(T v, TreeNode<T> cur) {
        return v.compareTo(cur.getValue())<0 && cur.getLeft()!=null;
    }

    protected boolean needRight(T v, TreeNode<T> cur) {
        return v.compareTo(cur.getValue())>0 && cur.getRight()!=null;
    }


    /**
     * 替换节点
     * @param node
     * @param newNode
     */
    protected void replace(TreeNode<T> node,TreeNode<T> newNode) {
        TreeNode pre=node.getPre();
        if(pre!=null){
            if(pre.getLeft()==node){
                pre.setLeft(newNode);
            }else{
                pre.setRight(newNode);
            }
        }else{
            replaceRoot(newNode);
        }
    }
}
