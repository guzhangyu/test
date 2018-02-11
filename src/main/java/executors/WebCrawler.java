package executors;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 网页爬虫
 * Created by guzy on 16/7/8.
 */
public abstract class WebCrawler {

    private volatile TrackingExecutor exec;

    private final Set<URL> urlsToCrawl=new HashSet<URL>();

    private final Integer TIMEOUT=5;

    private final TimeUnit UNIT=TimeUnit.MINUTES;

    private Integer curIndex=0;

    /**
     * 处理上次未处理完的数据
     */
    public synchronized void start(){
        curIndex++;
        exec=new TrackingExecutor(Executors.newCachedThreadPool());
        for(URL url:urlsToCrawl){
            submitCrawlTask(url);
        }
        urlsToCrawl.clear();
    }

    /**
     * 停止执行任务并将未执行的任务保存起来
     * @throws InterruptedException
     */
    public synchronized void stop() throws InterruptedException{
        try{
            saveUncrawled(exec.shutdownNow());
            if(exec.awaitTermination(TIMEOUT,UNIT)){
                saveUncrawled(exec.getCancelledTasks());
            }
        }finally {
            exec=null;
        }
    }

    /**
     * 从url地址中解析出子url列表
     * @param url
     * @return
     */
    protected abstract List<URL> processPage(URL url);

    /**
     * 将未爬取的url保存起来
     * @param uncrawled
     */
    private void saveUncrawled(List<Runnable> uncrawled){
        for(Runnable task:uncrawled){
            urlsToCrawl.add(((CrawlTask) task).getPage());
        }
    }

    public void addUrlsToCrawl(String url){
        try {
            urlsToCrawl.add(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 多线程对url进行爬取
     * @param url
     */
    private void submitCrawlTask(URL url){
        exec.execute(new CrawlTask(url));
    }

    /**
     * 爬虫任务
     */
    private class CrawlTask implements Runnable{
        private final URL url;

        public CrawlTask(URL url) {
            this.url = url;
        }

        public void run(){
            try {
                Thread.sleep(100l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(Thread.currentThread().isInterrupted()){
                return;
            }
            System.out.println(curIndex+":"+url);
            List<URL> urls=processPage(url);
            if(urls!=null && urls.size()>0){
                for(URL link:urls){
                    if(Thread.currentThread().isInterrupted()){
                        return;
                    }
                    submitCrawlTask(link);
                }
            }
        }

        public URL getPage(){
            return url;
        }
    }

    public static void main(String[] args) {

    }

}
