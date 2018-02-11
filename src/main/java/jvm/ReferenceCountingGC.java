package jvm;

/**
 * 垃圾回收机制测试
 * Created by guzy on 16/10/8.
 */
public class ReferenceCountingGC {

    public Object instance =null;

    private static final int _1MB=1024*1024;

    /**
     * 用于占空间，测试gc效果
     */
    private byte[] bigSize =new byte[2*_1MB];

    public static void testGc(){
        ReferenceCountingGC objA=new ReferenceCountingGC();
        ReferenceCountingGC objB=new ReferenceCountingGC();

        objA.instance=objB;
        objB.instance=objA;

        objA=null;
        objB=null;

        System.gc();
    }

    public static void main(String[] args) {
        testGc();
    }
}
