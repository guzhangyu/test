package tests;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by guzy on 16/7/16.
 */
public class LogPrinter {

    public BlockingQueue<String> logs=new LinkedBlockingDeque<String>();

    public void addLog(String log){
        logs.add(log);
    }

    public void printLog(){
        while(true){
            try {
                System.out.println(logs.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
