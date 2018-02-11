package jvm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by guzy on 16/10/21.
 */
public class Wait {

    /**
     * 死循环演示
     */
    public static void createBusyThread(){
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                while(true);
            }
        },"testBusyThread");
        thread.start();
    }


    /**
     * 锁等待
     * @param lock
     */
    public static void createLockThread(final Object lock){
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock){
                    try{
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br =new BufferedReader(new InputStreamReader(System.in));
        br.readLine();
        createBusyThread();
        br.readLine();
        createLockThread(new Object());
    }
}
