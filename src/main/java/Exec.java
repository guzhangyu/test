import java.util.concurrent.CountDownLatch;

/**
 * Created by guzy on 16/6/22.
 */
public interface Exec {

    public void exec(int i,CountDownLatch latch) throws InterruptedException;
}
