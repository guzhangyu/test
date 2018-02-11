package jvm;

import java.lang.invoke.*;

import static java.lang.invoke.MethodHandles.lookup;

class GrandFather{
    void think(){
        System.out.println("grandFather");
    }
}

class Father extends GrandFather{
    void think(){
        System.out.println("father");
    }


}

class Son extends Father{
    void think(){
       // super.think();
        try {
            INDY_BootstrapMethod("think",MethodType.fromMethodDescriptorString("()V",null)).invokeExact((GrandFather)this);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    /**
     * 根据查找范围、方法名、方法参数来得到调用桩
     * @param lookup 查找范围
     * @param name 方法名
     * @param mt 方法类型
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     */
    public static CallSite bootStrapMethod(MethodHandles.Lookup lookup,String name,MethodType mt,Class cls) throws NoSuchMethodException, IllegalAccessException {
        return new ConstantCallSite(lookup.findVirtual(cls,name,mt));
    }

    /**
     * 上面这个方法的方法类型
     * @return
     */
    private static MethodType MT_bootStrapMethod(){
        return MethodType.fromMethodDescriptorString("(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/Class;)Ljava/lang/invoke/CallSite;",null);
    }

    /**
     * 获取最上面方法的调用桩
     * @return
     */
    private static MethodHandle MH_BootstrapMethod() throws NoSuchMethodException, IllegalAccessException {
        return lookup().findStatic(Son.class,"bootStrapMethod",MT_bootStrapMethod());
    }

    public static MethodHandle INDY_BootstrapMethod(String methodName,MethodType mt) throws Throwable{
        CallSite cs=(CallSite)MH_BootstrapMethod().invokeWithArguments(lookup()
                ,methodName,mt,GrandFather.class
//                ,"testMethod"
//                , MethodType.fromMethodDescriptorString("(Ljava/lang/String;)V",null)
        );
        return cs.dynamicInvoker();
    }
}

/**
 * Created by guzy on 16/12/28.
 */
public class InvokeDynamicTest extends Father{

    public static void main(String[] args) throws Throwable {
       // INDY_BootstrapMethod().invokeExact("fdafdafa");
        new Son().think();
    }

    public static void testMethod(String s){
        System.out.println("hello:"+s);
    }

    /**
     * 根据查找范围、方法名、方法参数来得到调用桩
     * @param lookup 查找范围
     * @param name 方法名
     * @param mt 方法类型
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     */
    public static CallSite bootStrapMethod(MethodHandles.Lookup lookup,String name,MethodType mt) throws NoSuchMethodException, IllegalAccessException {
        return new ConstantCallSite(lookup.findStatic(InvokeDynamicTest.class,name,mt));
    }

    /**
     * 上面这个方法的方法类型
     * @return
     */
    private static MethodType MT_bootStrapMethod(){
        return MethodType.fromMethodDescriptorString("(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;",null);
    }

    /**
     * 获取最上面方法的调用桩
     * @return
     */
    private static MethodHandle MH_BootstrapMethod() throws NoSuchMethodException, IllegalAccessException {
        return lookup().findStatic(InvokeDynamicTest.class,"bootStrapMethod",MT_bootStrapMethod());
    }

    public static MethodHandle INDY_BootstrapMethod(String methodName,MethodType mt) throws Throwable{
        CallSite cs=(CallSite)MH_BootstrapMethod().invokeWithArguments(lookup()
                ,methodName,mt
//                ,"testMethod"
//                , MethodType.fromMethodDescriptorString("(Ljava/lang/String;)V",null)
        );
        return cs.dynamicInvoker();
    }
}
