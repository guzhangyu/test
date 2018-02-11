package executors;

import java.net.URL;
import java.util.List;

/**
 * Created by guzy on 16/7/8.
 */
public class WebCrawlerImpl extends WebCrawler {
    @Override
    protected List<URL> processPage(URL url) {
        return null;
    }

    public static void main(String[] args) {
        WebCrawler webCrawler=new WebCrawlerImpl();
        for(int i=0;i<50000;i++){
            webCrawler.addUrlsToCrawl("http://www.baidu.com/"+i);
        }
        webCrawler.start();
//        try {
//            Thread.sleep(1l);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        try {
            webCrawler.stop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("===============================================================================");

        webCrawler.start();
    }
}
