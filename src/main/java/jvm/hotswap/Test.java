package jvm.hotswap;

/**
 * Created by guzy on 16/12/29.
 */
public class Test {

    public void test(){
        System.out.println("ddsds");
    }

    public static void main(String[] args) {
        System.out.println("hahaha");
        new Test().test();
    }
}
