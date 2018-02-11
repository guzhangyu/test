import java.io.File;
import java.util.concurrent.BlockingQueue;

/**
 * Created by guzy on 16/6/25.
 */
public class FileIndexer implements Runnable {

    BlockingQueue<File> blockingQueue;

    public FileIndexer(BlockingQueue<File> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        while(true){
            try {
                File file=blockingQueue.take();
                System.out.println(file.getName()+",");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
