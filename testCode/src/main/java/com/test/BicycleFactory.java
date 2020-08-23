package com.test;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

public interface BicycleFactory {
    Bicycle newBicycle();
}

class AimaBicycleFactory implements BicycleFactory {

    @Override
    public Bicycle newBicycle() {
        return new AimaBicycle();
    }

    public static int[] sort(int[] arr){
        for(int i=0, j=arr.length-1; i<j; ){
            while(i<j && arr[i]>0){
                i++;
            }
//            if(i>=j){
//                break;
//            }

            while(i<j && arr[j]<=0){
                j--;
            }
            if(i!=j){
                swap(arr, i, j);
            }
        }
        return arr;
    }

    public static void swap(int[] arr, int pre, int after){
        int temp = arr[pre];
        arr[pre] = arr[after];
        arr[after] = temp;
    }

    public static void main(String[] args) throws IOException {
//        new AimaBicycleFactory().newBicycle().run();
//        int[] A = { 1 , 3 , -1 ,0 , 2 , 1 , -4 , 2 , 0 ,1, 5};
//        sort(A);
//        for (int a : A){
//            System.out.print(a+" ");
//        }

        ClassLoader systemLoader = ClassLoader.getSystemClassLoader();
        System.out.println("系统类加载器: " + systemLoader);

        Enumeration<URL> em1 = systemLoader.getResources("");
        while(em1.hasMoreElements()){
            System.out.println("SystemClassLoader Route: " + em1.nextElement()); //系统类加载器的加载路径，是程序运行的当前路径
        }

        ClassLoader extentionLoader = systemLoader.getParent();
        System.out.println("扩展类加载器： " + extentionLoader);
        System.out.println("扩展类加载器的路径： " + System.getProperty("java.ext.dirs"));

        ClassLoader baseLoader = extentionLoader.getParent();
        System.out.println("启动类加载器: " + baseLoader);
    }
}

class YadeaBicycleFactory implements BicycleFactory {

    @Override
    public Bicycle newBicycle() {
        return new YadeaBicycle();
    }
}

interface Bicycle {
    void run();
}

class AimaBicycle implements Bicycle {

    @Override
    public void run() {
        System.out.println("Hello, I'm Aima.");
    }
}

class YadeaBicycle implements Bicycle {

    @Override
    public void run() {
        System.out.println("Hello, I'm Yadea.");
    }
}

