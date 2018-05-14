package interview;

public class A{
    int d=1;
    static int c=1;
    public A(){
        d=2;
    }
    static{
        System.out.println(String.format("A c:%d",c));
    }
    static{
        c=2;
    }
    public void test(){
        System.out.println(String.format("A run c:%d,d:%d",c,this.d));
    }

    public static void main(String[] args){
        new B().test();
    }
}

class B extends A{
    int d=3;
    static int c=3;
    public B(){
        d=4;
    }
    static{
        c=4;
    }
    static{
        System.out.println(String.format("B c:%d",c));
    }

    public void test(){
        super.test();
        System.out.println(String.format("B run c:%d,d:%d",c,d));
    }
}
