package jvm;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

public class TestWeakReference {

    private static volatile boolean isRun=true;

    private static volatile ReferenceQueue<String> referenceQueue=new ReferenceQueue<>();

    public static void main(String[] args) throws InterruptedException {
        String abc=new String("abc");

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(isRun){
                    Reference w=referenceQueue.poll();

                    if(w!=null){
                        try {
                            Field field=Reference.class.getDeclaredField("referent");
                            field.setAccessible(true);
                            Object o=field.get(w);
                            System.out.println(String.format("%s,%s",o.getClass(),o.hashCode()));
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();

        PhantomReference<String> weakReference=new PhantomReference<>(abc,referenceQueue);
        abc=null;

        System.gc();
        Thread.sleep(3000l);
        isRun=false;

    }
}
