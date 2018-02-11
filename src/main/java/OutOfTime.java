import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by guzy on 16/6/27.
 */
public class OutOfTime {

    public static void main(String[] args) {
        Timer timer=new Timer();
        timer.schedule(new ThrowTask(),1);
//        SECONDS.sleep(1);
//
//        timer.schedule(new ThrowTask(),1);
//        SECONDS.sleep(5);
    }

    static class ThrowTask extends TimerTask{

        @Override
        public void run() {
            throw new RuntimeException();
        }
    }
}
