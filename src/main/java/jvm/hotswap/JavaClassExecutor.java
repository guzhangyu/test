package jvm.hotswap;

import java.io.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guzy on 16/12/29.
 */
public class JavaClassExecutor {

    public static int a(List<Integer> a){
        assert a.size()>0;
        if(true){
            System.out.println("d");
        }else{
            System.out.println("c");
        }
        return 1;
    }

    public static void execute(byte[] classBytes) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        HotswapClassLoader loader=new HotswapClassLoader();
        Class clazz=loader.loadBytes(classBytes);
        //Method method=clazz.getMethod("main");
        //method.invoke(null);
//        Method method=clazz.getDeclaredMethod("main", String[].class);
//        method.invoke(null,new String[]{null});

        try {
            MethodHandles.lookup().findStatic(clazz,"main", MethodType.methodType(void.class,String[].class))
                    .invoke(new String[]{""});
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException {
        FileInputStream fi=new FileInputStream("/Users/guzy/IdeaProjects/raycloud/myTest/target/classes/jvm/hotswap/reflect.Test.class");
        //BufferedReader br=new BufferedReader(new InputStreamReader(fi));
        int len=fi.available();
        byte[]b=new byte[len];
        fi.read(b);
        //JavaClassExecutor.execute(b);

        System.out.println(a(new ArrayList<Integer>()));
    }
}
