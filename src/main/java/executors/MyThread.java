package executors;

import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by guzy on 16/7/11.
 */
public class MyThread extends Thread {

    public static final String DEFAULT_NAME="MyAppThread";

    private static volatile boolean debugLifeCycle=false;

    private static final AtomicInteger created = new AtomicInteger();

    private static final AtomicInteger alive = new AtomicInteger();

    private static final Logger logger = Logger.getAnonymousLogger();



    public MyThread(Runnable runnable, String poolName) {
        super(runnable,poolName+"-"+created.incrementAndGet());
        setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler(){

            @Override
            public void uncaughtException(Thread t, Throwable e) {
                logger.log(Level.SEVERE,"UNCAUGHT in thread "+t.getName(),e);
            }
        });
    }

    public void run(){
        boolean debug=debugLifeCycle;
        if(debug){
            logger.log(Level.FINE,"created "+getName());
        }
        try{
            alive.incrementAndGet();
            super.run();
        }finally {
            alive.decrementAndGet();
            if(debug){
                logger.log(Level.FINE,"exiting "+getName());
            }
        }
    }

    public static AtomicInteger getAlive() {
        return alive;
    }

    public static boolean isDebugLifeCycle() {
        return debugLifeCycle;
    }

    public static void setDebugLifeCycle(boolean debugLifeCycle) {
        MyThread.debugLifeCycle = debugLifeCycle;
    }

    public static AtomicInteger getCreated() {
        return created;
    }
}
