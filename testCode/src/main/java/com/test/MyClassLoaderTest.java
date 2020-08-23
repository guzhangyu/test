package com.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class MyClassLoaderTest extends URLClassLoader {

    String javaFilePath;

    String classFilePath;

    public MyClassLoaderTest(String projectPath) throws MalformedURLException {
        super(new URL[]{new URL("file:/"+projectPath + "src/main/java/")}, MyClassLoaderTest.getSystemClassLoader());
        this.javaFilePath = projectPath + "src/main/java/";
        this.classFilePath = projectPath + "target/classes/";
    }

    private byte[] getBytes(String filename) throws IOException {
        System.out.println("使用getBytes方法");
        File file = new File(filename);
        long len = file.length();
        byte[] raw = new byte[(int)len];

        try(FileInputStream fis = new FileInputStream(file)){
            // 一次读取class文件的全部二进制数据
            int r = fis.read(raw);
            if(r!=len)
                throw new IOException("无法读取全部文件：" + r + "!=" + len);
            return raw;
        }
    }

    /**
     * 编译指定java文件，返回编译的结果
     * @param javaFile
     * @return
     */
    private boolean compile(String javaFile) throws IOException{
        System.out.println("myClassLoader:正在编译" + javaFile + "............");
        Process p = Runtime.getRuntime().exec("javac -d " + classFilePath + " " + javaFile);

        try {
            p.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int ret = p.exitValue();
        return ret == 0;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class clazz = null;
        name = name.replaceAll("\\.","/");
        String javaFilename = javaFilePath + name + ".java";
        String classFilename = classFilePath + name + ".class";

        File javaFile = new File(javaFilename);
        File classFile = new File(classFilename);

        if(javaFile.exists() && (!classFile.exists() || javaFile.lastModified() > classFile.lastModified())){
            try{
                if(!compile(javaFilename) || !classFile.exists()){
                    throw new ClassNotFoundException("ClassNotFoundException: " + javaFilename + " or " + classFilename + " not exists ");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        if(classFile.exists()){
            try{
                byte[] raw = getBytes(classFilename);
                clazz = defineClass(name.replaceAll("/","."), raw, 0, raw.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(clazz==null){
            throw new ClassNotFoundException(name);
        }
        return clazz;
    }

    public static void main(String[] args) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, MalformedURLException {
        String javaFilePath = "/Users/zhangyugu/IdeaProjects/practice-Code1/";
        MyClassLoaderTest myClassLoaderTest = new MyClassLoaderTest(javaFilePath);
        String fullClassName = "com.test.Hello";
        String className = "com.test.Hello";

        Class<?> clazz1 = myClassLoaderTest.findLoadedClass(fullClassName);

        Class<?> clazz = myClassLoaderTest.loadClass(className, false);
        Class<?> clazz2 = myClassLoaderTest.findLoadedClass(fullClassName);

        Method main = clazz.getMethod("main", (new String[0].getClass()));
        String[] progArgs = {""};
        Object argsArray[] = {progArgs};
        main.invoke(null, argsArray);
    }
}
