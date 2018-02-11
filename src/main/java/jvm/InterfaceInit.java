package jvm;

/**
 * Created by guzy on 16/12/27.
 */
public class InterfaceInit {
    public static void main(String[] args) {
        //InterfaceSub.a=4;
        System.out.println(InterfaceSub.a);
    }
}

interface InterfaceInit1{

}

class InterfaceSub1 implements InterfaceSub{
    static{
        System.out.println("sub1 init ");
    }
}

class InterfaceSub2 extends InterfaceSub1 implements InterfaceInit2{

}

interface InterfaceInit2{
    int a=4;
}

interface InterfaceSub extends InterfaceInit1{
    int a=3;
}


