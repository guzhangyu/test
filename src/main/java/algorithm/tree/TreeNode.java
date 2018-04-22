package algorithm.tree;

import com.sun.xml.internal.ws.util.StringUtils;

import java.util.Stack;
import java.util.regex.Pattern;

/**
 * 树节点
 * Created by guzy on 18/2/7.
 */
public class TreeNode<T> implements Cloneable{

    private TreeNode<T> left;

    private TreeNode<T> right;

    private TreeNode<T> parent;

    private T value;

    //用于打印
    private int level;

    //用于打印以及平衡二叉树的维护
    private int leftDepth=0;

    private int rightDepth=0;

    private int color;

    private static final int COLOR_RED=0,COLOR_BLACK=1;

     int lFlag=0;//0 表示正常的左子树/右子树  1 表示前驱/后继
     int rFlag=0;


    /**
     * 这个建立在原有的数据正确的基础上
     * @param right
     * @return
     */
    public TreeNode setRight(TreeNode<T> right) {
        this.right = right;
        if(right!=null){
            right.setParent(this);
            rightDepth= getHigherDepth(right);
        }else{
            rightDepth=0;
        }
        updateParentDepth();
        return this;
    }

    public TreeNode setLeft(TreeNode<T> left) {
        this.left = left;
        if(left!=null){
            left.setParent(this);
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
        TreeNode<T> curPre= parent;
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
            curPre=curPre.getParent();
        }
    }

    public static int getHigherDepth(TreeNode right) {
        return Math.max(right.getLeftDepth(), right.getRightDepth())+1;
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
        newNode.setColor(this.getColor());
        return newNode;
    }

    /**
     * Created by guzy on 18/2/8.
     */
    public interface TreeNodeDealer<T,V> {
        V deal(TreeNode<T> node);
    }

    private  <V> V traverseByFlag(TreeNode<T> head,TreeNode.TreeNodeDealer<T, V> dealer) {
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

    private TreeNode<T> createTracedTree(){
        TreeNode<T> head=new TreeNode<>(null);
        head.left=this;

        TreeNode<T> p=null;
        Stack<TreeNode<T>> stack=new Stack<>();
        stack.push(this);
        TreeNode<T> pre=this;
        while(!stack.isEmpty()){
            while((p=stack.peek())!=null){
                if(p.left==null){
                    p.left=pre;
                    p.lFlag=1;

                }

                if(pre.right==null){
                    pre.rFlag=1;
                    pre.right=p;
                }

                stack.push(p.left);
                pre=stack.peek();
            }
            stack.pop();
            if(!stack.isEmpty()){//如果根节点已去
                pre=stack.pop();
                stack.push(pre.right);
            }
        }
        head.right=pre;
        head.lFlag=1;
        head.rFlag=1;
        return head;
    }



    public <V> V iterate(TreeNodeDealer<T,V> dealer){
        TreeNode<T> head=createTracedTree();
        return traverseByFlag(head,dealer);
        //先序
//        V v= dealer.deal(this);
//        if(this.left!=null){
//            v=this.left.iterate(dealer);
//        }
//        if(this.right!=null){
//            v=this.right.iterate(dealer);
//        }
//        return v;
        //return preOrderTraverse(dealer);

        //中序
//        V v=null;
//        if(this.left!=null){
//            v=this.left.iterate(dealer);
//        }
//        v= dealer.deal(this);
//
//        if(this.right!=null){
//            v=this.right.iterate(dealer);
//        }
//        return v;

//        Stack<TreeNode<T>> s=new Stack<>();
//        s.push(this);
//        TreeNode<T> p=null;
//        V v=null;
//
//        while(!s.empty()){
//            while((p=s.peek())!=null){//
//                s.push(p.getLeft());
//            }
//            s.pop();//这两处是巧妙的关键
//
//            if(!s.empty()){
//                p=s.pop();
//                v=dealer.deal(p);
//                s.push(p.getRight());
//            }
//        }
//        return v;
    }

    private <V> V preOrderTraverse(TreeNodeDealer<T, V> dealer) {
        V v=null;
        TreeNode<T> p=null;
        Stack<TreeNode<T>> stack=new Stack<>();
        stack.push(this);
        while(!stack.isEmpty()){
            while((p=stack.peek())!=null){
                v=dealer.deal(p);
                stack.push(p.left);
            }
            stack.pop();
            if(!stack.isEmpty()){//如果根节点已去
                stack.push(stack.pop().right);
            }
        }
        return v;
    }

    private <V> V preOrderTraverseByFlag(TreeNodeDealer<T, V> dealer) {
        V v = null;
        TreeNode<T> p=this;

        while(p!=this.right){
            while(p!=null && p.lFlag==0){
                v=dealer.deal(p);
                p=p.left;
            }
            p=p.right;
        }

        return v;
    }

    public <V> V iterateNoCur(TreeNodeDealer<T,V> dealer){
       return null;
    }

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


    final static Pattern trimPat=Pattern.compile("\\.0$");

    public String toString(){
        return String.format("%s,%s",trimPat.matcher(getValue()+"").replaceFirst(""),isRed()?"r":"b");
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

    public TreeNode<T> getParent() {
        return parent;
    }

    public TreeNode setParent(TreeNode<T> parent) {
        this.parent = parent;
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

    public TreeNode<T> getRight() {
        return right;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public int getLFlag() {
        return lFlag;
    }

    public void setLFlag(int lFlag) {
        this.lFlag = lFlag;
    }

    public int getRFlag() {
        return rFlag;
    }

    public void setRFlag(int rFlag) {
        this.rFlag = rFlag;
    }

    public static void main(String[] args) {
        System.out.println(StringUtils.capitalize("lFlag"));
    }
}
