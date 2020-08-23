package com.test;

public class ClassInit extends ParentClassInit {

    private static int c=4;

    static {
        System.out.println("c:"+c);
    }

    public static void main(String[] args) {
        new ClassInit();
    }
}
