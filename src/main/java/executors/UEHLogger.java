package executors;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by guzy on 16/7/10.
 */
public class UEHLogger implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Logger logger= Logger.getAnonymousLogger();
        logger.log(Level.SEVERE,
                "Thread terminated with exception1: "+t.getName(),
                e);
    }

    public static void main(String[] args) {

    }
}
