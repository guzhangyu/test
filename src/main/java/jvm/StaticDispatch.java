package jvm;

/**
 * Created by guzy on 16/12/28.
 */
public class StaticDispatch {

    static abstract class Human{

    }

    static class Man extends Human{

    }

    static class Woman extends Human{

    }

    public void sayHello(Man h){
        System.out.println("hello,man");
    }
    public void sayHello(Woman h){
        System.out.println("hello,woman");
    }

    public void sayHello(Human h){
        System.out.println("hello,human");
    }

    public static void main(String[] args) {
        StaticDispatch sd=new StaticDispatch();
        Human h=null;
        h=new Man();
        sd.sayHello((Man)h);
        //h=new Woman();
        sd.sayHello((Woman)h);
    }
}
