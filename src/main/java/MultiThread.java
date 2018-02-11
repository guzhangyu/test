import java.util.concurrent.CountDownLatch;

/**
 * Created by guzy on 16/6/22.
 */
public class MultiThread {

    int n;
    public MultiThread(int n){
        this.n=n;
    }

    int i;
    public void exec(final Exec exec){
        final CountDownLatch latch=new CountDownLatch(n);
        for(i=0;i<n;i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        exec.exec(i,latch);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            latch.countDown();
        }
    }
}
