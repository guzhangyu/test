import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by guzy on 16/6/30.
 */
public class IndexingService {

    private static final File POISON=new File("");

    private final IndexerThread consumer=new IndexerThread();

    private final CrawlerThread producer=new CrawlerThread();

    private final BlockingQueue<File> queue;

    private final FileFilter fileFilter;

    private final File root;

    public IndexingService(){
        queue=new ArrayBlockingQueue<File>(10);
        fileFilter=new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return false;
            }
        };
        root=new File(".");
    }

    class CrawlerThread extends Thread{
        public void run(){
            try {
                crawl(root);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                while(true){
                    try {
                        queue.put(POISON);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        //retry code
                    }
                    break;
                }
            }
        }

        private void crawl(File root) throws InterruptedException{

        }
    }

    class IndexerThread extends Thread{
        public void run(){
            try {
                while(true) {
                    File file = null;
                    file = queue.take();
                    if (file == POISON){
                        break;
                    }else{
                            //indexFile(file);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    public void start(){
        producer.start();
        consumer.start();
    }

    public void stop(){
        producer.interrupt();
    }

    public void awaitTermination() throws InterruptedException{
        consumer.join();
    }
}
