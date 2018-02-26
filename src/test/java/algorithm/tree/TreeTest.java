package algorithm.tree;

import org.junit.Test;

import java.util.Random;

/**
 * Created by guzy on 18/2/26.
 */
public class TreeTest {

    BinarySearchTree tree;

    static int len=10800;
    static int arr[]=new int[len];
    static{
        for(int i=0;i<len;i++){
            arr[i]=new Random(len).nextInt();
        }
    }

    public void test(){

        long begin=System.currentTimeMillis();
        for(int i:arr){
            tree.insert(i);
        }
        System.out.println(String.format("init use time:%d",(System.currentTimeMillis()-begin)));

        begin=System.currentTimeMillis();

        for(int i=0;i<len;i++){
            int j=new Random().nextInt(len);
            tree.find(arr[j]);
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
