package jvm;

/**
 * Created by guzy on 16/12/30.
 */
public class VolatileTest {

    public static volatile int a=0;

    public static void test(){
        a++;
    }

    public static void main(String[] args) {
        test();
    }
}
