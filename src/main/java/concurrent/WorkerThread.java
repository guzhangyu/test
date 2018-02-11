package concurrent;

import java.util.concurrent.BlockingQueue;

/**
 * Created by guzy on 16/7/16.
 */
public class WorkerThread extends Thread {

    private final BlockingQueue<Runnable> tasks;

    public WorkerThread( BlockingQueue<Runnable> tasks) {
        this.tasks = tasks;
    }

    @Override
    public void run() {
        while(true){
            try {
                tasks.take().run();
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
