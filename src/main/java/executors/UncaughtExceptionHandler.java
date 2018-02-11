package executors;

/**
 * Created by guzy on 16/7/10.
 */
public interface UncaughtExceptionHandler {
    void uncaughtException(Thread t ,Throwable e);
}
