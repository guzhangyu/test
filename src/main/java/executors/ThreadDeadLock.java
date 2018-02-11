package executors;

import java.util.concurrent.*;

/**
 * Created by guzy on 16/7/10.
 */
public class ThreadDeadLock {

    public static void main(String[] args) {
        final ExecutorService es= Executors.newSingleThreadExecutor();

        es.submit(new Runnable() {
            @Override
            public void run() {
                Future<String> str=es.submit(new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return "testStr";
                    }
                });
                try {
                    System.out.println(str.get()+"unreachable");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
