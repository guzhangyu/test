package tests;

import com.sun.deploy.util.ArrayUtil;

import java.util.Arrays;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;

/**
 * Created by guzy on 16/7/16.
 */
public class BoundedBuffer<E> {

    private final Semaphore availableItems,availableSpaces;

    private final E[]items;

    private final AtomicInteger putPosition=new AtomicInteger(0),takePosition=new AtomicInteger(0);

    volatile int count=0;

    final LogPrinter lp=new LogPrinter();

    public Boolean isFull(){
        return availableSpaces.availablePermits()==0;
    }

    public Boolean isEmpty(){
        return availableItems.availablePermits()==0;
    }

    public BoundedBuffer(int capacity){
        availableItems=new Semaphore(0);
        availableSpaces=new Semaphore(capacity);
        items=(E[])new Object[capacity];
        new Thread(new Runnable() {
            @Override
            public void run() {
                lp.printLog();
            }
        }).start();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while(count<100){
//                    try {
//                        Thread.sleep(100l);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                System.exit(0);
//            }
//        }).start();
    }

    public void put(E e) throws InterruptedException {
        availableSpaces.acquire();
        doInsert(e);
        availableItems.release();
    }

    public E take() throws InterruptedException {
        availableItems.acquire();
        E e= doExtract();
        availableSpaces.release();
        return e;
    }



    private void doInsert(E x){
        String text=null;
        int index=putPosition.getAndUpdate(new IntUnaryOperator() {
            @Override
            public int applyAsInt(int operand) {
                return operand==items.length-1?0:operand+1;
            }
        });
        items[index]=x;
        text=String.format("[%d]put[%d,%s]",count++,index,x);
        //lp.addLog(text);
    }

    private E doExtract(){
        E e=null;
        String text=null;
        int index=takePosition.getAndUpdate(new IntUnaryOperator() {
            @Override
            public int applyAsInt(int operand) {
                return operand==items.length-1?0:operand+1;
            }
        });
        e=items[index];
        text=String.format("[%d]take[%d,%s]",count++,index,e);
        items[index]=null;
       // lp.addLog(text);
        return e;
    }

    public void printInfo(){
        synchronized (items){
            lp.addLog(String.format("[%d]take:%d,put:%d,es:%s",count++,takePosition.get(),putPosition.get(), Arrays.toString(items)));
        }
    }

}
