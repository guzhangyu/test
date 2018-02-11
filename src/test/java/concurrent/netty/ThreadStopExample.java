package concurrent.netty;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Created by guzy on 16/9/26.
 */
public class ThreadStopExample {

    private static boolean stop;

    @Test
    public void testVolatile() throws InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i=0;
                while(!stop){
                    i++;
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        TimeUnit.SECONDS.sleep(3);
        stop=true;
    }

    public static void main(String[] args) throws InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i=0;
                while(!stop){
                    i++;
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        TimeUnit.SECONDS.sleep(3);
        stop=true;
    }
}
