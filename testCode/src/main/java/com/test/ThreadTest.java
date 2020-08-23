package com.test;

import java.util.concurrent.CountDownLatch;




public class ThreadTest {
    static boolean stop=false;

    static int i=0;

//    public static void main(String[] args) {
////        Object a=new Object(),b=new Object(),c=new Object();
////        new Thread(new Runnable() {
////            @Override
////            public void run() {
////                while(!stop){
////                    System.out.println("a");
////                    synchronized (b){
////                        b.notify();
////                    }
////                    synchronized (a){
////                        try {
////                            a.wait();
////                        } catch (InterruptedException e) {
////                            e.printStackTrace();
////                        }
////                    }
////                }
////            }
////        }).start();
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while(!stop){
//                    synchronized(ThreadTest.class) {
//                        if(i%3==0){
//                            System.out.println("a");
//                            i++;
//                        }
//                    }
//                }
//            }
//        }).start();
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while(!stop){
//                    synchronized(ThreadTest.class) {
//                        if(i%3==1){
//                            System.out.println("b");
//                            i++;
//                        }
//                    }
//                }
//            }
//        }).start();
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while(!stop){
//                    synchronized(ThreadTest.class) {
//                        if(i%3==2){
//                            System.out.println("c");
//                            i++;
//                        }
//                    }
//                }
//            }
//        }).start();
//    }

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch=new CountDownLatch(4);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("2");
            }
        }).start();

        for(int i=0;i<4;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000l);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    latch.countDown();
                }
            }).start();
        }

        latch.await();
        System.out.println("1");

    }
}
