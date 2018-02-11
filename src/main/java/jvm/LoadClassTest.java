package jvm;

/**
 * Created by guzy on 16/12/27.
 */
public class LoadClassTest {


    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("jvm.LoadClass", false, new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                System.out.println("hahaha   ");
                return super.loadClass(name);
            }
        });
    }
}

class LoadClass{
    static{
        System.out.println("init loadClass");
    }
}
