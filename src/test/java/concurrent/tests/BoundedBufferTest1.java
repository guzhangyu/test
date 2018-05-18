package concurrent.tests;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by guzy on 16/7/16.
 */

public class BoundedBufferTest1 {

    final BoundedBuffer<Integer> bb=new BoundedBuffer<Integer>(4);

    @Test
    public void testEmpty(){
        bb.printInfo();
        Assert.assertTrue(bb.isEmpty());
    }

    @Test
    public void testFull() throws InterruptedException {
        bb.put(1);
        bb.put(2);
        bb.put(3);
        bb.take();
        bb.put(1);
        bb.put(2);
        Assert.assertTrue(bb.isFull());
        bb.printInfo();
    }

    @Test
    public void testOne() throws InterruptedException {
        bb.put(3);
        Integer a=bb.take();
        Assert.assertEquals(a.intValue(), 3);
        bb.printInfo();
    }

    @Test
    public void testThree() throws InterruptedException {
        bb.put(1);
        bb.put(2);
        bb.put(3);
        bb.printInfo();
        bb.take();
    }

    public final int LOCKUP_DETECT_TIMEOUT=1000;

    @Test
    public void testTakeBlocksWhenEmpty(){
        final BoundedBuffer<Integer> bb=new BoundedBuffer<Integer>(10);
        Thread taker =new Thread(){
            public void run(){
                try {
                    int unused=bb.take();
                    fail();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        try{
            taker.start();
            Thread.sleep(LOCKUP_DETECT_TIMEOUT);
            taker.interrupt();
            taker.join(LOCKUP_DETECT_TIMEOUT);
            Assert.assertFalse(taker.isAlive());
        }catch(Exception unexpected){
            fail();
        }
    }

    @Test
    public void testTakeAfterBlock(){
        final BoundedBuffer<Integer> bb=new BoundedBuffer<Integer>(10);
        Thread taker =new Thread(){
            public void run(){
                try {
                    int unused=bb.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        try{
            taker.start();
            Thread.sleep(LOCKUP_DETECT_TIMEOUT);
            bb.put(3);
            Thread.sleep(1);
            taker.interrupt();
            taker.join(LOCKUP_DETECT_TIMEOUT);
            Assert.assertFalse(taker.isAlive());
        }catch(Exception unexpected){
            fail();
        }
    }

    private void fail() {
        System.out.println("----------fail-----------");
    }


    static long xorShift(long y){
        y^=(y<<6);
        y^=(y>>>21);
        y^=(y<<7);
        return y;
    }

    @Test
    public void testXorShift(){
        System.out.println(xorShift(System.currentTimeMillis()));
    }

    @Test
    public void testArrToList(){
        //List<Integer> list=CollectionUti
    }
}
