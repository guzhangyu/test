package jvm;


import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by guzy on 16/12/28.
 */
public class StaticResolution {

    public static void sayHello() throws IOException {
        System.out.println("Hello world");
        FilterOutputStream ps=
                new PrintStream(System.out){
                    @Override
                    public void write(byte[] b) throws IOException {

                    }
                };
        ps.write("dd".getBytes());
//        PrintInterface ps=null;
//
//        //PrintImpl pi=new PrintImpl();
//        InvocationHandler ih=new MyInvokeHandler();
//        ps= (PrintInterface)Proxy.newProxyInstance(PrintImpl.class.getClassLoader(),PrintImpl.class.getInterfaces(),ih);
//        ps.println("hah");
    }

    public static void main(String[] args) throws IOException {
        sayHello();
    }
}

interface PrintInterface{
    void println(String str);
}

class PrintImpl implements PrintInterface{

    @Override
    public void println(String str) {

    }
}

class MyInvokeHandler implements InvocationHandler {

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        return null;
    }
}
