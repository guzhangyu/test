package algorithm.tree;

import algorithm.tree.b.BMinusTree;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by guzy on 18/2/26.
 */
public class TreeTest {

    Tree tree;
    TreePrintTool printTool=new TreePrintTool();

    static int len=10;
    static Integer[]arr=new Integer[len];
    static{
        Set<Integer> s=new HashSet<Integer>();
        for(int i=0;i<len;i++){
            while(!s.add(new Random().nextInt(len*100)));
        }
        arr=s.toArray(new Integer[len]);
    }

    public void test(){

        long begin=System.currentTimeMillis();
        for(Integer i:arr){
            tree.insert(i);
        }
//        try {
//            printTool.printTree(tree.tree,new FileOutputStream("/Users/guzy/Desktop/"+tree.getClass().getSimpleName()));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        System.out.println(String.format("init use time:%d",(System.currentTimeMillis()-begin)));

        begin=System.currentTimeMillis();

        for(int i=0;i<len;i++){
            int j=new Random().nextInt(len);
            //System.out.println(String.format("begin find:%d",arr[j]));
            //tree.find(arr[j]);
            System.out.println(String.format("i:%d,v:%d,find:%s",i,arr[j],tree.find(arr[j])));
            tree.remove(arr[j]);

        }
        System.out.println(String.format("remove use time:%d",(System.currentTimeMillis()-begin)));
    }

    @Test
    public void testRedBlackTree(){
        tree=new RedBlackTree();
        System.out.println("redBlack");
        test();
    }


    @Test
    public void testBMinusTree(){
        tree=new BMinusTree(3);
        System.out.println("testBMinusTree");
        test();
    }

    @Test
     public void testBalancedBinarySearchTree(){
        tree=new BalancedBinarySearchTree();
        System.out.println("BalancedBinarySearchTree");
        test();
    }


    @Test
    public void testBinarySearchTree(){
        tree=new BinarySearchTree();
        System.out.println("BinarySearchTree");
        test();
    }

}
