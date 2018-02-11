package jmm;

/**
 * Created by guzy on 16/7/26.
 */
public class PossibleRecording {

    static int x=0,y=0;
    static int a=0,b=0;

    public static void main(String[] args) throws InterruptedException {
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
               x=1;
                a=b;
            }
        });
       Thread t1= new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                b=1;
                y=x;
            }
        });
        t1.start();t.start();
        t.join();t1.join();
        System.out.println(String.format("(%d,%d)",a,y));
    }
}
