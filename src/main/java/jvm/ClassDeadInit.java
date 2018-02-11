package jvm;

/**
 * Created by guzy on 16/12/27.
 */
public class ClassDeadInit {



    public static void main(String[] args) throws InterruptedException {
        Runnable run=new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread()+" start");
                ClassDeadInit1 classDeadInit=new ClassDeadInit1();
                System.out.println(Thread.currentThread()+" end");
            }
        };
        Thread t=new Thread(run);
        Thread t2=new Thread(run);
        t.start();
        Thread.sleep(2000l);
        t2.start();
    }
}

class ClassDeadInit1{
    static{
        if(true){
            System.out.println(Thread.currentThread()+" init DeadLoopClass ");
            while(true){

            }
        }
    }
}
