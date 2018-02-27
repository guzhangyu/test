package algorithm.tree;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 树打印工具类
 * 实现思路：递归得出每个子树的打印字符串
 *   v
 *  / \
 * l   r
 * Created by guzy on 18/2/25.
 */
public class TreePrintTool<T> {

    int maxSize=0;

    class Node4Print{
        int width=0;

       // int height=1;

        List<String> strs=new ArrayList<String>();
    }

    protected void printTree(TreeNode<T> tree,OutputStream out){
        this.maxSize=tree.iterate(new TreeNode.TreeNodeDealer<T,Integer>() {

            @Override
            public Integer deal(TreeNode<T> node) {
                maxSize= Math.max(maxSize,getNodeStr(node).length());
                return maxSize;
            }

            int maxSize=0;
        });

        Node4Print node4Print=toPrintNode(tree);

        for(String str:node4Print.strs) {
            try {
                out.write((str+"\n").getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void printTree(TreeNode<T> tree){
        printTree(tree,System.out);
    }

    /**
     * 打印节点信息
     * @param node
     * @return
     */
    private Node4Print toPrintNode(TreeNode<T> node){
        Node4Print node4Print=new Node4Print();
        if(node==null){
            return node4Print;
        }

        if(node.isLeaf()){
            node4Print.width=maxSize;
            node4Print.strs.add(getNodeStr(node));
            return node4Print;
        }

        Node4Print left=toPrintNode(node.getLeft());
        Node4Print right=toPrintNode(node.getRight());

        node4Print.strs.add( (left.width==0?"":getBlank(left.width+1)) + getNodeStr(node) +getBlank(right.width+1));//head
        int height=Math.max(left.strs.size(),right.strs.size());

        String ob = "";
        int maxDept=Math.max(node.getLeftDepth(),node.getRightDepth());
        for(int i=0;i<maxDept-1;i++){
            ob+=" ";
        }

        // /
        String leftStr="";
        if(left.width!=0){
            leftStr=getBlank(left.width-maxDept+1)+"/"+ob;//Math.round(left.width*3/4)
        }

        // \
        String rightStr="";
        if(right.width!=0){
            rightStr=getBlank(right.width/2)+"\\ ";
        }
        String blank = getBlank(maxSize);
        node4Print.strs.add( leftStr+blank+ rightStr);

        //底下的内容
        String leftBlank=getBlank(left.width);
        for(int i=0;i<height;i++){
            if(left.width!=0){
                String s=getStr(left, i);
                if(s.equals("")){
                    s=leftBlank;
                }
                leftStr=s+ " "+ob;
            }
            if(right.width!=0){
                rightStr= " " +getStr(right, i);
            }
            node4Print.strs.add(leftStr+blank+rightStr);
        }

        fixWidth(node4Print);
        System.out.println(String.format("v:%s,w:%d",node.getValue(),node4Print.width));
        return node4Print;
    }

    /**
     * 统一并设置width
     * @param node4Print
     */
    private void fixWidth(Node4Print node4Print) {
        int w=0;
        for(String s:node4Print.strs){
            if(w<s.length()){
                w=s.length();
            }
        }
        for(int i=0;i<node4Print.strs.size();i++){
            if(node4Print.strs.get(i).length()<w){
                String s=node4Print.strs.get(i);
                s+=getBlank(w-s.length());
                node4Print.strs.set(i,s);
            }
        }
        node4Print.width=w;
    }

    private String getStr(Node4Print node4Print,int index){
        if(node4Print.strs.size()<=index){
            return "";
        }
        return node4Print.strs.get(index);
    }

    private String getNodeStr(TreeNode<T> node) {
//        if(node.getValue()==null){
//            return " ";
//        }
        String s= node.toString();
        while(s.length()<maxSize-1){
            s=" "+s+" ";
        }
        if(s.length()<maxSize){
            s=" "+s;
        }
        return s;
    }



    private String getBlank(int vLen) {
        if(vLen<=0){
            return "";
        }
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<vLen;i++){
            sb.append(" ");
        }
        return sb.toString();
    }
}
