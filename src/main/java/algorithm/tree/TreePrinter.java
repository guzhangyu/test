package algorithm.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 树打印器
 * Created by guzy on 18/2/7.
 */
@Deprecated
public class TreePrinter<T> {

    static String BLOCK=" ";

    static int maxSize=0;

    final static Pattern trimPat=Pattern.compile("\\.0$");

    //final static Pattern endBlankPat=Pattern.compile("( *[^ ]+ +)");


    public void printTree(TreeNode<T> n){
        TreeNode<T> root = getFullTree(n);

        BLOCK=" ";
        int maxSize=n.iterate(new TreeNode.TreeNodeDealer<T,Integer>() {

            @Override
            public Integer deal(TreeNode<T> node) {
                maxSize= Math.max(maxSize,getVSize(node));
                return maxSize;
            }

            int maxSize=0;
        });
        System.out.println(String.format("maxSize:%d",maxSize));
        for(int i=0;i<(maxSize-1)/2;i++){
            BLOCK+=" ";
        }
        this.maxSize=maxSize;

        List<StringBuffer> list=new ArrayList<StringBuffer>();

        createPrintStr(root, list,false);
//
//
//
//        StringBuffer sb=new StringBuffer(root.getValue().toString());
//        list.add(sb);
//        sb.insert(0,BLOCK+BLOCK).append(BLOCK);
//
//        StringBuffer sb1=new StringBuffer("/ \\");
//        list.add(sb1);
//        sb1.insert(0,BLOCK).append(BLOCK);
//
//        StringBuffer sb2=new StringBuffer(root.getLeft().getValue().toString()).append(BLOCK+BLOCK+BLOCK).append(root.getRight().getValue().toString());
//        list.add(sb2);
        for(StringBuffer s:list){
            System.out.println(s);
        }
    }

    private void createPrintStr(TreeNode<T> node,List<StringBuffer> list,boolean right){
        if(node==null){
            return;
        }

        StringBuffer sb = getOrAddStr(list, node.getLevel()*2+1);

        int count=countByBlank(sb.toString());
        if(count==0 || count%2!=0){
            sb.append(BLOCK + BLOCK);
        }else{
            sb.append(BLOCK);
        }

        if(!node.isEmpty()) {
            sb.append(fillStrLen(formatNodeV(node) +" "+node.getColor(), maxSize));
            if(!right){
                sb.append(BLOCK);
            }
        }

        StringBuffer sb2=getOrAddStr(list, node.getLevel()*2+2);

        sb2.append(BLOCK);
        if(node.getLeft()!=null && !node.getLeft().isEmpty()){
            sb2.append("/"+BLOCK);
        }else{
            sb2.append(BLOCK+BLOCK);
        }

        if(node.getRight()!=null && !node.getRight().isEmpty()){
            sb2.append("\\"+BLOCK);
        }else{
            sb2.append(BLOCK+BLOCK);
        }

        if(node.getLeft()!=null){
            for(StringBuffer s:list){
                s.insert(0,BLOCK+BLOCK);
            }
        }

        createPrintStr(node.getLeft(),list,false);
        createPrintStr(node.getRight(),list,true);

    }

    private TreeNode<T> getFullTree(TreeNode<T> n) {
        TreeNode<T> root=n.clone();
        setLevel(root,0);
        fullFill(root);
        return root;
    }


    private TreeNode<T> fullFill(TreeNode<T> node){
        int len=Math.max(node.getLeftDepth(),node.getRightDepth());
        fill(node,len);
        return node;
    }

    private TreeNode<T> fill(TreeNode<T> node,int len){
        int level=node.getLevel();
        if(level<len){
            if(node.getLeft()==null){
                node.setLeft(new TreeNode<T>(null).setLevel(level + 1));
            }
            fill(node.getLeft(),len);

            if(node.getRight()==null){
                node.setRight(new TreeNode<T>(null).setLevel(level + 1));
            }
            fill(node.getRight(),len);
        }
        return node;
    }

    private int getVSize(TreeNode<T> node){
        if(node==null || node.getValue()==null){
            return 0;
        }
        return formatNodeV(node).length();
    }

    public static String formatNodeV(TreeNode node) {
        return trimPat.matcher(node.getValue().toString()).replaceFirst("");
    }

    private void setLevel(TreeNode<T> node,int level){
        if(node==null){
            return;
        }
        node.setLevel(level);
        setLevel(node.getLeft(),level+1);
        setLevel(node.getRight(),level+1);
    }

    private String fillStrLen(String str,int len){
        return str;
//        if(str.length()==len){
//            return str;
//        }
//        StringBuffer sb=new StringBuffer(str);
//        int left=(len-str.length())/2;
//        for(int i=0;i<left;i++){
//            sb.append(" ");
//        }
//        for(int i=0;i<len-left-str.length();i++){
//            sb.insert(0," ");
//        }
//        return sb.toString();
    }

    private static int countByBlank(String str){
        String[] arr=str.split("\\s+");
        int count=0;
        for(String s:arr){
            if(s.length()>0){
                count++;
            }
        }
        return count;
    }

    /**
     * 获取父的左右差
     * @param node
     * @return
     */
    private int getParentDiff(TreeNode<T> node){
        int diff=0;
        TreeNode<T> pre=node.getPre();
        while(pre!=null){
            if(pre.getLeft()==node){
                diff-=1;
            }else{
                diff+=1;
            }
            node=pre;
            pre=node.getPre();
        }
        return diff;
    }

    /**
     * 拿到第num个字符串
     * @param list
     * @param num
     * @return
     */
    private StringBuffer getOrAddStr(List<StringBuffer> list, int num) {
        StringBuffer sb=null;
        if(list.size()<num){
            sb=new StringBuffer();
            list.add(sb);
        }else{
            sb=list.get(num-1);
        }
        return sb;
    }

    public static void main(String[] args) {
//        Matcher matcher = endBlankPat.matcher("0.9     1.1  ");
//        matcher.find();
//        System.out.println(matcher.toMatchResult().groupCount());

        System.out.println(countByBlank("44 fdafd "));
//        Map<String,Object> json=new HashMap<String, Object>();
//        json.put("d","fdafda");
//        for(Map.Entry<String,Object> entry:json.entrySet()){
//            if(entry.getValue()!=null && entry.getValue() instanceof String){
//                String str=(String)entry.getValue();
//                if(str.length()>3){
//                    entry.setValue(str.substring(0,2));
//                }
//            }
//        }
//        System.out.println(json);
    }
}
