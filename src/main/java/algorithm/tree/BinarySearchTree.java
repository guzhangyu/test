package algorithm.tree;

/**
 * 二叉查找树
 * Created by guzy on 18/2/7.
 */
public class BinarySearchTree<T extends Comparable> {

    TreeNode<T> tree;


    /**
     * 查找某个节点
     *
     * @param v
     * @return
     */
    public TreeNode<T> find(T v) {
        return find(tree, v);
    }

    /**
     * 查找某个节点
     *
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

        if(node.getLeft()==null && node.getRight()==null){
            replace(node,null);
        }else if(node.getLeft()==null){
            replace(node,node.getRight());
        }else if(node.getRight()==null){
            replace(node,node.getLeft());
        }else{//用后继节点替代
            TreeNode<T> rightNext=node.getRight();
            while(rightNext.getLeft()!=null){
                rightNext=rightNext.getLeft();
            }
            if(rightNext!=node.getRight()){
                rightNext.getPre().setLeft(rightNext.getRight());
            }
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

    protected boolean needLeft(T v, TreeNode<T> cur) {
        return v.compareTo(cur.getValue())<0 && cur.getLeft()!=null;
    }

    protected boolean needRight(T v, TreeNode<T> cur) {
        return v.compareTo(cur.getValue())>0 && cur.getRight()!=null;
    }


    protected void replace(TreeNode<T> node,TreeNode<T> newNode) {
        TreeNode pre=node.getPre();
        if(pre!=null){
            if(pre.getLeft()==node){
                pre.setLeft(newNode);
            }else{
                pre.setRight(newNode);
            }
        }
    }
}
