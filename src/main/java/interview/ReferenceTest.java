package interview;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * Created by guzy on 2018-04-03.
 */
public class ReferenceTest {
    public static void soft(){
        Object obj=new Object();
        ReferenceQueue refQueue=new ReferenceQueue();
        SoftReference softReference=new SoftReference(obj,refQueue);
        System.out.println(softReference.get());
        System.out.println(refQueue.poll());

        obj=null;
        System.gc();
        System.out.println(softReference.get());

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(refQueue.poll());
    }

    public static void weak(){
        Object obj=new Object();
        ReferenceQueue refQueue=new ReferenceQueue();
        WeakReference weakReference=new WeakReference(obj,refQueue);
        System.out.println(weakReference.get());
        System.out.println(refQueue.poll());

        obj=null;
        System.gc();
        System.out.println(weakReference.get());

//        try {
//            Thread.sleep(10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        System.out.println(refQueue.poll());
    }

    public static void main(String[] args) {
        weak();
    }
}
