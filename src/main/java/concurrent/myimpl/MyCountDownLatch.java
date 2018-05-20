package concurrent.myimpl;

public interface MyCountDownLatch {

    void await() throws InterruptedException;

    void countDown();
}
