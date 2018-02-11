package jvm;

import java.io.Serializable;

/**
 * Created by guzy on 16/12/28.
 */
public class Overload {

//    public static void sayHello(int a){
//        System.out.println("int");
//    }
//
////    public static void sayHello(char a){
////        System.out.println("char");
////    }
//    public static void sayHello(Character a){
//        System.out.println("character");
//    }

    public static void sayHello(Object a){
        System.out.println("object");
    }

    public static void sayHello(Serializable a){
        System.out.println("Serializable");
    }

//    public static void sayHello(long a){
//        System.out.println("long");
//    }


    public static void main(String[] args) {
        sayHello('a');
    }

}
