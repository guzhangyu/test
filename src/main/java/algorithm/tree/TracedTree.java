package algorithm.tree;

/**
 * Created by Administrator on 2018/4/20/020.
 */
public class TracedTree<T> {

    TreeNode<T> head;
//
//    TreeNode<T> root;

    public  <V> V traverseByFlag(TreeNode.TreeNodeDealer<T, V> dealer) {
        V v = null;
        TreeNode<T> p=head.getLeft();

        while(p!=head.getRight()){
            while(p!=null && p.lFlag==0){
                v=dealer.deal(p);
                p=p.getLeft();
            }
            p=p.getRight();
        }

        return v;
    }


}
