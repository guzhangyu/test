package jvm;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.reflect.Field;

public class FreeMemoryTask implements Runnable {

    private static volatile ReferenceQueue<Data> referenceQueue=new ReferenceQueue<>();


    @Override
    public void run() {
        while (true){
           Reference<? extends Data> reference =referenceQueue.poll();
           if(reference!=null){
               Field rereferent = null;
               try {
                   rereferent = Reference.class.getDeclaredField("referent");
                   rereferent.setAccessible(true);
                   //Object result = rereferent.get(o);

                   Data data=(Data) rereferent.get(reference);
                   data.deallocate();
                   break;
               } catch (NoSuchFieldException e) {
                   e.printStackTrace();
               } catch (IllegalAccessException e) {
                   e.printStackTrace();
               }

           }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        FreeMemoryTask task=new FreeMemoryTask();
        new Thread(task).start();

        Data data=new Data();
        PhantomReference<Data> weakReference=new PhantomReference<Data>(data,referenceQueue);
        data=null;

        System.gc();
        Thread.sleep(1000l);

    }
}

class Data{

    public void deallocate(){
        System.out.println("deallocate");
    }
}
