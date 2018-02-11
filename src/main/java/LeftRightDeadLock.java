/**
 * Created by guzy on 16/7/14.
 */
public class LeftRightDeadLock {

    private final Object left=new Object();

    private final Object right=new Object();

    public void leftRight(){
        synchronized (left){
            try {
                Thread.sleep(1000l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (right){

            }
        }
    }

    public void rightLeft(){
        synchronized (right){
            try {
                Thread.sleep(1000l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (left){

            }
        }
    }

    public static void main(String[] args) {
        final LeftRightDeadLock lock=new LeftRightDeadLock();
        new Thread(new Runnable() {
            @Override
            public void run() {
                lock.leftRight();
            }
        }).start();
        lock.rightLeft();
    }
}
