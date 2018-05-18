package jvm;

public class JoinTest {

    volatile static int a,b,x,y;

    public static void main(String[] args) throws InterruptedException {
        for(int i=0;i<100;i++){
            a=b=y=x=0;
            Thread t1=new Thread(new Runnable() {
                @Override
                public void run() {
//                    try {
//                        Thread.sleep(1000l);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }

                    a=1;
                    y=b;

//                    try {
//                        Thread.sleep(1000l);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                }
            });
            Thread t2=new Thread(new Runnable() {
                @Override
                public void run() {
//                    try {
//                        Thread.sleep(1000l);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    b=1;
                    x=a;

//                    try {
//                        Thread.sleep(1000l);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                }
            });
            t1.start();
            t2.start();
            t1.join();
            t2.join();
            System.out.println(String.format("x:%d,y:%d",x,y));
        }
    }
}
