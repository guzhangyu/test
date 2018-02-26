package algorithm.tree;

/**
 * 平衡二叉查找树
 * Created by guzy on 18/2/7.
 */
public class BalancedBinarySearchTree<T extends Comparable> extends BinarySearchTree<T>{

    /**
     * 按顺序依次插入构建二叉查找树
     * @param arr
     * @return
     */
    public TreeNode<T> createSequencely(T[]arr){
        if(arr==null||arr.length==0){
            return null;
        }

        for(int i=0;i<arr.length;i++){
            super.insert(arr[i]);
        }
        return this.tree;
    }

    @Override
    public void remove(T v) {
        super.remove(v);
        balance(tree);
    }

    @Override
    public TreeNode<T> insert(T v) {
        TreeNode<T> result= super.insert(v);
        balance(tree);
        return result;
    }

    /**
     * 先排序，后构造二叉平衡查找树
     * @param arr
     * @return
     */
    public TreeNode<T> createBalanceTree(T[]arr){
        //Object[] sortedArr=new Object[arr.length];
        sort(arr);
        TreeNode<T> t= arrToNode(arr,0,arr.length-1);
        this.tree=t;
        return t;
    }

    private void balance(TreeNode<T> tree){
        if(tree==null){
            return;
        }
        while(tree.getLeftDepth()-tree.getRightDepth()>1){
            if(tree.getLeft().getLeftDepth()-tree.getLeft().getRightDepth()>0){
                tree=balanceLL(tree);
            }else{
                tree=balanceLR(tree);
            }
        }

        while(tree.getRightDepth()-tree.getLeftDepth()>1){
            if(tree.getRight().getRightDepth()-tree.getRight().getLeftDepth()>0){
                tree=balanceRR(tree);
            }else{
                tree=balanceRL(tree);
            }
        }

        if(tree.getLeft()!=null){
            balance(tree.getLeft());
        }
        if(tree.getRight()!=null){
            balance(tree.getRight());
        }
    }

    private TreeNode<T> balanceLL(TreeNode<T> node){
        TreeNode<T> newRoot=node.getLeft();
       // replace(node, newRoot);

        rightDown(node,newRoot);
//        node.setLeft(newRoot.getRight());
//        newRoot.setRight(node);

        return newRoot;
    }

    private TreeNode<T> balanceRR(TreeNode<T> node){
        TreeNode<T> newRoot=node.getRight();
        //replace(node, newRoot);

        leftDown(node,newRoot);
//        node.setRight(newRoot.getLeft());
//        newRoot.setLeft(node);

        return newRoot;
    }

    /**
     * 用根节点的右子树的最左节点，来替代跟节点
     * @param node
     * @return
     */
    private TreeNode<T> balanceRL(TreeNode<T> node){
        TreeNode<T> newRoot=node.getRight().getLeft();
        replace(node,newRoot);

        node.getRight().setLeft(newRoot.getRight());
        newRoot.setRight(node.getRight());
        node.setRight(newRoot.getLeft());
        newRoot.setLeft(node);

        return newRoot;
    }

    private TreeNode<T> balanceLR(TreeNode<T> node){
        TreeNode<T> newRoot=node.getLeft().getRight();
        replace(node, newRoot);

        node.getLeft().setRight(newRoot.getLeft());
        newRoot.setLeft(node.getLeft());
        node.setLeft(newRoot.getRight());
        newRoot.setRight(node);

        return newRoot;
    }

    /**
     * 将arr从小到大排序
     * @param arr
     */
    private void sort(T[] arr) {
        for(int i=0;i<arr.length-1;i++){
            int index=i;
            for(int j=i+1;j<arr.length;j++){
                if(arr[index].compareTo(arr[j])>0){
                    index=j;
                }
            }
            if(index!=i){
                T mid=arr[index];
                arr[index]=arr[i];
                arr[i]=mid;
            }
        }
    }

    /**
     * 有序数组转为平衡二叉树
     * @param arr
     * @param start
     * @param end
     * @return
     */
    private TreeNode<T> arrToNode(T[]arr,int start,int end){
        int mid=(start+end)/2;
        TreeNode<T> node=new TreeNode<T>(arr[mid]);
        if(mid>start){
            node.setLeft(arrToNode(arr,start,mid-1));
        }
        if(mid<end){
            node.setRight(arrToNode(arr, mid + 1, end));
        }
        return node;
    }


    public static void main(String[] args) {
        BalancedBinarySearchTree<Double> searchTree=new BalancedBinarySearchTree<Double>();
        TreeNode<Double> root=
//                new TreeNode<Double>(6)
//                    .setLeft(new TreeNode<Double>(3)
//                            .setLeft(new TreeNode<Double>(1)
//                                    .setRight(new TreeNode<Double>(2)))
//                            .setRight(new TreeNode(4))
//                            )
//                    .setRight(new TreeNode(7));



//                new TreeNode<Double>(1d)
//                    .setLeft(new TreeNode<Double>(2d)
//                            .setLeft(new TreeNode<Double>(3d)
//                                        .setLeft(new TreeNode<Double>(5d))
//                                        .setRight(new TreeNode(6D)))
//                            .setRight(new TreeNode<Double>(4D)))
//                    .setRight(new TreeNode<Double>(7d)
//                            .setLeft(new TreeNode<Double>(3d))
//                            .setRight(new TreeNode<Double>(4D)));


//        new TreeNode<Double>(6d)
//                .setLeft(new TreeNode<Double>(2d)
//                        .setLeft(new TreeNode(1d)
//                                .setLeft(new TreeNode<Double>(0.9d)
//                                        .setLeft(new TreeNode<Double>(0.8d)))
//                                .setRight(new TreeNode(1.1d)))
//                        .setRight(new TreeNode<Double>(4d)
//                                .setLeft(new TreeNode<Double>(3d))
//                                .setRight(new TreeNode(5d))))
//                .setRight(new TreeNode(7d));

                searchTree.createSequencely(new Double[]{13d, 4d, 5d,2d,7d,1.1d,5.3d,6.7d,7.7d,});//

//        root.setLeft(new TreeNode<Double>(1).setLeft(new TreeNode<Double>(0).setRight(
//                new TreeNode<Double>(5).setRight(new TreeNode<Double>(5)))));
//        root.setRight(new TreeNode<Double>(3));

        TreePrintTool<Double> treePrinter=new TreePrintTool<Double>();
        treePrinter.printTree(root);

        //searchTree.remove(root, 7);
//        root=searchTree.balance(root);
//        root.setPre(null);
//        treePrinter.printTree(root);
    }
}
