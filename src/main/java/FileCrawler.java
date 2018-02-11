import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by guzy on 16/6/25.
 */
public class FileCrawler implements Runnable {

    BlockingQueue<File> fileQueue;

    FileFilter fileFilter;

    File root;

    void crawl(File file){
        if(file==null || !file.exists()){
            System.out.println("file not exist");
        }
        File[] files= file.listFiles(fileFilter);
        if(files!=null && files.length>0){
            for(File f:files){
                if(f.isDirectory()){
                   crawl(f);
                }else{
                    try {
                        fileQueue.put(f);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else{
            System.out.println("no file satisfy the condition");
        }
    }

    @Override
    public void run() {
        crawl(root);
    }

    public FileCrawler(BlockingQueue<File> fileQueue, FileFilter fileFilter, File root) {
        this.fileQueue = fileQueue;
        this.fileFilter = fileFilter;
        this.root = root;
    }

    public static void main(String[] args) {
        BlockingQueue<File> blockingQueue=new ArrayBlockingQueue<File>(20);
        FileCrawler crawler=new FileCrawler(blockingQueue, new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                //System.out.println(pathname);
                return pathname.length()>7;
            }
        },new File("/Users/guzy"));
        FileIndexer indexer=new FileIndexer(blockingQueue);
        new Thread(indexer).start();
        new Thread(crawler).start();
    }
}
