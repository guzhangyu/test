package jvm;

/**
 * Created by guzy on 16/10/20.
 */
public class MinorGC {

    private static final int _1MB=1024*1024;

    /**
     * -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8
     */
    public static void testAllocation(){
        byte[] all1,all2,all3,all4;
        all1=new byte[2*_1MB];
        all2=new byte[2*_1MB];
        all3=new byte[2*_1MB];
        all4=new byte[4*_1MB];
    }

    /**
     * -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:PretenureSizeThreshold=3145728
     */
    public static void testPretenureSizeThreshold(){
        byte[] all4;
        all4=new byte[4*_1MB];
    }

    /**
     * -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:PretenureSizeThreshold=3145728
     */
    public static void testTenuringThreshold(){
        byte[] all1,all2,all3;
        all3=new byte[_1MB/4];
        //什么时候进入老年代取决于XX:MaxTenuringThreshold设置

        all3=new byte[4*_1MB];
        all3=new byte[4*_1MB];
        all3=null;
        all3=new byte[4*_1MB];
    }

    /**
     * -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=15 -XX:+PrintTenuringDistribution
     */
    public static void testTenuringThreshold2(){
        byte[] all1,all2,all3,all4;
        all1=new byte[_1MB/4];
        //all1+all2大于 survivor空间一半
        all2=new byte[_1MB/4];
        all3=new byte[_1MB*4];
        all4=new byte[_1MB*4];
        all4=null;
        all4=new byte[_1MB*4];
    }


    public static void main(String[] args) {
        testTenuringThreshold2();
    }
}
