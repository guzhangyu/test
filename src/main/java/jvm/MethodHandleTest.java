package jvm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;


/**
 * Created by guzy on 16/12/28.
 */
public class MethodHandleTest {

    static class ClassA{
        public void println(String s){
            System.out.println(s);
        }
    }

    class GrandFather1{
        void think(){
            System.out.println("grandFather");
        }
    }

    class Father1 extends GrandFather1{
        void think(){
            System.out.println("father");
        }


    }

    class Son1 extends Father1{
        void think(){
            try {
                //System.out.println("test ");
                MethodHandle mh= MethodHandles.lookup()
                        .findSpecial(GrandFather1.class, "think", MethodType.methodType(void.class),Son1.class)
                        .bindTo(this);
                //System.out.println("test1 ");
                //mh.invoke();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

    }

    public static void main(String[] args) throws Throwable {
        new MethodHandleTest(). new Son1().think();
//        Object o=System.currentTimeMillis()%2==0?System.out:new ClassA();
//        System.out.println("before ");
//        MethodHandleTest mht=new MethodHandleTest();
//        mht.dd();
//        mht.getPrintlnMH(o).invokeExact("fdafda");

    }

    public MethodHandle getPrintlnMH(Object o,MethodType mt,String methodName) throws NoSuchMethodException, IllegalAccessException {
        return MethodHandles.lookup()
                .findSpecial(GrandFather1.class, methodName, mt, Son1.class)
                .bindTo(o);
    }

    public MethodHandle getPrintlnMH(Object o) throws NoSuchMethodException, IllegalAccessException {
        MethodType mt= MethodType.methodType(void.class, String.class);
        System.out.println("testhh");
        return MethodHandles.lookup()
                .findVirtual(o.getClass(), "println", mt)
                .bindTo(o);
    }

    public void dd(){
        System.out.println("dd");
    }
}
