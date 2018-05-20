package concurrent.myimpl;

import sun.misc.Unsafe;

import java.text.ParseException;

public class MyAtomicInteger {

    static Unsafe unsafe=Unsafe.getUnsafe();

    static long aOffset;

    static{
        try {
            aOffset=unsafe.objectFieldOffset(MyAtomicInteger.class.getField("a"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private int a;

    public MyAtomicInteger(int a) {
        this.a = a;
    }

    public boolean compareAndSet(int before, int after){
        return unsafe.compareAndSwapInt(this,aOffset,before,after);
    }

    public int getA() {
        return a;
    }

    public static void main(String[] args) throws ParseException {
//        MyAtomicInteger mai=new MyAtomicInteger(3);
//        mai.compareAndSet(3,4);
//        System.out.println(mai.a);

    }
}
