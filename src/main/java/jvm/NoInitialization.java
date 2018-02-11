package jvm;

/**
 * Created by guzy on 16/12/27.
 */
class SuperClass{
    static{
        System.out.println("superClass init");
    }

    public final static int value=12;
}

class SubClass extends SuperClass{

    static{
        System.out.println("SubClass init");
    }
}
public class NoInitialization {

    public static void main(String[] args) {
       System.out.println(SubClass.value);
        //SuperClass[] cls=new SuperClass[10];
    }
}
