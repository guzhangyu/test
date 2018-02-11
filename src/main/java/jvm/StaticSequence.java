package jvm;

/**
 * Created by guzy on 16/12/27.
 */
public class StaticSequence {

    public static void main(String[] args) {
        System.out.println(Sub.B);
    }
}
class Parent{
    public static int A =1;
    static{
        A=2;
    }
}

class Sub extends Parent{
    public static int B=A;
}