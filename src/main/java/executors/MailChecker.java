package executors;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by guzy on 16/7/2.
 */
public class MailChecker {

    boolean checkMail(Set<String> hosts,long timeout,TimeUnit unit) throws InterruptedException {
        ExecutorService exec= Executors.newCachedThreadPool();
        final AtomicBoolean hasNewMail=new AtomicBoolean(false);
        try{
            for(final String host:hosts){
                exec.execute(new Runnable() {
                    @Override
                    public void run() {
                        if(checkMail(host)){
                            hasNewMail.set(true);
                        }
                    }
                });
            }
        }finally {
            exec.shutdown();
            exec.awaitTermination(timeout,unit);
        }
        return hasNewMail.get();
    }

    private boolean checkMail(String host) {
        Random random=new Random(100);
        return random.nextInt()%2==0;
    }

    public static void main(String[]args){
        MailChecker mailChecker=new MailChecker();
        Set<String> hosts=new HashSet<String>();
        hosts.add("343");
        //hosts.add("33");
        try {
            System.out.println(mailChecker.checkMail(hosts,1000,TimeUnit.DAYS));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
