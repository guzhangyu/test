package jvm;


import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

class GCTarget{
    public String id;

    byte[] buffer=new byte[1024];

    public GCTarget(String id) {
        this.id = id;
    }

    @Override
    protected void finalize() throws Throwable {
        System.out.println("Finalizing GCTarget,id is: "+id);
    }
}

class GCTargetWeakReference extends WeakReference<GCTarget>{

    public String id;

    public GCTargetWeakReference(GCTarget referent, ReferenceQueue<? super GCTarget> q) {
        super(referent, q);
        this.id=referent.id;
    }

    @Override
    protected void finalize() throws Throwable {
        System.out.println("Finalizing GCTargetWeakReference "+id);
    }
}
public class TestWeakReference1 {


    private final static ReferenceQueue<GCTarget> REFERENCE_QUEUE= new ReferenceQueue<>();

    public static void main(String[] args) {
        LinkedList<GCTargetWeakReference> gcTargetList = new LinkedList<>();

        for(int i=0;i<5;i++){
            GCTarget gcTarget=new GCTarget(String.valueOf(i));
            GCTargetWeakReference weakReference=new GCTargetWeakReference(gcTarget,REFERENCE_QUEUE);
            gcTargetList.add(weakReference);

            System.out.println("Just created GCTargetWeakReference obj: "+gcTargetList.getLast());
        }

        System.gc();

        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Reference<? extends GCTarget> reference;
        while((reference=REFERENCE_QUEUE.poll())!=null){
            if(reference instanceof GCTargetWeakReference){
                System.out.println("In queue, id is: "+
                        ((GCTargetWeakReference)reference).id);
            }
        }
    }
}
