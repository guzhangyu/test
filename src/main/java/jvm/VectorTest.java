package jvm;

import java.util.Vector;

/**
 * Created by guzy on 17/1/10.
 */
public class VectorTest {

    private static Vector<Integer> vector=new Vector<Integer>();

    public static void main(String[] args) {
        while(true){
            for(int i=0;i<10;i++){
                vector.add(i);
            }
            Thread removeThread=new Thread(new Runnable() {
                @Override
                public void run() {
                    int size=vector.size();
//                    try {
//                        Thread.sleep(1000l);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    for(int i=0;i<size;i++){
                        vector.remove(0);
                    }
                    //System.out.println(vector.size());
                }
            });
            Thread printThread=new Thread(new Runnable() {
                @Override
                public void run() {
                    for(int i=0;i<vector.size();i++){
                        //vector.get(i);
                        System.out.println(vector.get(i));
                    }
                }
            });
            removeThread.start();
            printThread.start();

            while(Thread.activeCount()>20);
        }
    }
}
