package algorithm.tree;

/**
 * Created by guzy on 18/2/7.
 */
public class TreeNode<T> implements Cloneable{

    private TreeNode<T> left;

    private TreeNode<T> right;

    private TreeNode<T> pre;

    //final static T EMPTY_V=new Object();

    private T value;

    private int level;

    private int leftDepth=0;

    private int rightDepth=0;

    private int color;

    private static final int COLOR_RED=0,COLOR_BLACK=1;


    public boolean isRed(){
        return color==COLOR_RED;
    }

    public void setRed(boolean red){
        if(red){
            setColor(COLOR_RED);
        }else{
            setColor(COLOR_BLACK);
        }
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getRightDepth() {
        return rightDepth;
    }

    public void setRightDepth(int rightDepth) {
        this.rightDepth = rightDepth;
    }

    public int getLeftDepth() {
        return leftDepth;
    }

    public void setLeftDepth(int leftDepth) {
        this.leftDepth = leftDepth;
    }

    public TreeNode<T> getPre() {
        return pre;
    }

    public TreeNode setPre(TreeNode<T> pre) {
        this.pre = pre;
        return this;
    }

    public int getLevel() {
        return level;
    }

    public TreeNode<T> setLevel(int level) {
        this.level = level;
        return this;
    }

    public TreeNode(T value) {
        this.value = value;
    }

    public Boolean isEmpty(){
        return value==null;
    }

    public TreeNode<T> getLeft() {
        return left;
    }

    public boolean isLeaf(){
        return left==null && right==null;
    }

    public TreeNode setLeft(TreeNode<T> left) {
        this.left = left;
        if(left!=null){
            left.setPre(this);
            leftDepth= getHigherDepth(left);
        }else{
            leftDepth=0;
        }
        updateParentDepth();
        return this;
    }

    /**
     * 依次向上更新层级，直到根节点，或者节点重复之前
     */
    private void updateParentDepth() {
        TreeNode<T> now=this;
        TreeNode<T> curPre=pre;
        while(curPre!=null && curPre!=this){
            int newDepth= getHigherDepth(now);
            if(curPre.getLeft()==now){
                if(curPre.getLeftDepth()==newDepth){
                    break;
                }
                curPre.setLeftDepth(newDepth);
            }else{
                if(curPre.getRightDepth()==newDepth){
                    break;
                }
                curPre.setRightDepth(newDepth);
            }
            now=curPre;
            curPre=curPre.getPre();
        }
    }

    public TreeNode<T> addLeft(T v){
        return setLeft(new TreeNode<T>(v));
    }

    public TreeNode<T> addRight(T v){
        return setRight(new TreeNode<T>(v));
    }

    public TreeNode<T> getRight() {
        return right;
    }

    /**
     * 这个建立在原有的数据正确的基础上
     * @param right
     * @return
     */
    public TreeNode setRight(TreeNode<T> right) {
        this.right = right;
        if(right!=null){
            right.setPre(this);
            rightDepth= getHigherDepth(right);
        }else{
            rightDepth=0;
        }
        updateParentDepth();
        return this;
    }

    private int getHigherDepth(TreeNode<T> right) {
        return Math.max(right.getLeftDepth(), right.getRightDepth())+1;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    protected TreeNode<T> clone() {
        TreeNode<T> newNode=new TreeNode<T>(this.getValue());
        if(this.getLeft()!=null){
            newNode.setLeft(this.getLeft().clone());
        }
        if(this.getRight()!=null){
            newNode.setRight(this.getRight().clone());
        }
        return newNode;
    }

    public <V> V iterate(TreeNodeDealer<T,V> dealer){
        V v= dealer.deal(this);
        if(this.left!=null){
            v=this.left.iterate(dealer);
        }
        if(this.right!=null){
            v=this.right.iterate(dealer);
        }
        return v;
    }
}
