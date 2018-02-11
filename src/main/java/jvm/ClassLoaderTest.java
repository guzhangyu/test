package jvm;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by guzy on 16/12/27.
 */
public class ClassLoaderTest {

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        ClassLoader cl=new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                String fileName=name.substring(name.lastIndexOf(".")+1)+".class";
                InputStream is=getClass().getResourceAsStream(fileName);
                if(is==null){
                    System.out.println(name+" file not found!");
                    return super.loadClass(name);
                }
                try {
                    byte []b=new byte[is.available()];
                    is.read(b);
                    return defineClass(name,b,0,b.length);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new ClassNotFoundException(name);
                }
            }


        };
        Class<ClassLoaderTest> t=(Class<ClassLoaderTest>)cl.loadClass("jvm.ClassLoaderTest");
        System.out.println(t==ClassLoaderTest.class);

        Object test=t.newInstance();

        System.out.println("is null:"+(test==null));
        System.out.println("instance:"+(test instanceof ClassLoaderTest));
        System.out.println("instance:"+(test.getClass()==t));
        System.out.println("instance:"+(test.getClass()==ClassLoaderTest.class));
       // System.out.println(ClassLoader.getSystemClassLoader());
    }
}
