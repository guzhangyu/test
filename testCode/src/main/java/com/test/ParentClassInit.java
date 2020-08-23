package com.test;

public class ParentClassInit {


    private static int a=1;

    private int b=1;

    static{
        System.out.println("a:"+a);
    }

    public ParentClassInit(){
        System.out.println(b);
    }
}
