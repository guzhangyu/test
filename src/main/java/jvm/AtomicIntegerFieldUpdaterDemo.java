package jvm;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class AtomicIntegerFieldUpdaterDemo {

    class DemoDataParent{
         volatile int a;
    }

    class DemoData extends DemoDataParent{
      //  volatile int a;
    }

    AtomicIntegerFieldUpdater<DemoData> getUpdater(String field){
        return AtomicIntegerFieldUpdater.newUpdater(DemoData.class,field);
    }

    public static void main(String[] args) {
        AtomicIntegerFieldUpdaterDemo demo=new AtomicIntegerFieldUpdaterDemo();
        AtomicIntegerFieldUpdaterDemo.DemoData demoData=demo.new DemoData();
        demoData.a++;
        System.out.println( demo.getUpdater("a").addAndGet(demoData,2));
    }
}
