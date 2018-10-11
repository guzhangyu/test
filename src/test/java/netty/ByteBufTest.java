package netty;

import org.junit.Test;

import java.nio.ByteBuffer;

public class ByteBufTest {

    ByteBuffer byteBuffer=ByteBuffer.allocate(1000);

//    @Test
//    public void limit(){
//        //System.out.println(byteBuffer.limit());
//        System.out.println(byteBuffer);
//        byteBuffer.limit(4);
//        System.out.println(byteBuffer);
//    }

    //position改为上次的
    @Test
    public void reset(){
        System.out.println(byteBuffer);
        byteBuffer.position(6);
        byteBuffer.mark();

        byteBuffer.position(7);
        byteBuffer.limit(88);
        byteBuffer.reset();
        System.out.println(byteBuffer);
    }


    //position改为0， limit改为cap
    @Test
    public void clear(){
        System.out.println(byteBuffer);

        byteBuffer.position(4);
        byteBuffer.limit(6);

        byteBuffer.clear();
        System.out.println(byteBuffer);
    }

    //position改为0,limit 不改
    @Test
    public void rewind(){
        System.out.println(byteBuffer);

        byteBuffer.position(4);
        byteBuffer.limit(6);

        byteBuffer.rewind();
        System.out.println(byteBuffer);
    }

    //postion改为limit-position，改为从该位置开始写
    @Test
    public void compact(){
        System.out.println(byteBuffer);

        byteBuffer.position(4);
        byteBuffer.limit(6);

        byteBuffer.compact();
        System.out.println(byteBuffer);
    }

    //从byteBuf的position位置开始读取len个数据到bytes中(从offset下标开始)
    @Test
    public void getL(){
        System.out.println(byteBuffer);

        byteBuffer.put("abcde".getBytes());
        System.out.println(byteBuffer);

        byteBuffer.rewind();

        byte[] bytes=new byte[5];
        byteBuffer.get(bytes,0,4);
        System.out.println(new String(bytes));

        System.out.println(byteBuffer);
    }

    /**
     * 从bytes offset开始读取len个元素，写入到byteBuffer中
     */
    @Test
    public void putL(){
        System.out.println(byteBuffer);

        byteBuffer.put("abcde".getBytes());
        System.out.println(byteBuffer);

        byteBuffer.put("abcdee".getBytes(),0,3);

        System.out.println(byteBuffer);

        System.out.println(new String(byteBuffer.array()));
        byteBuffer.putChar(0,'6');
        System.out.println(new String(byteBuffer.array()));
    }

    @Test
    public void putByteBuffer(){
        System.out.println(byteBuffer);

        byteBuffer.put("abcde".getBytes());
        ByteBuffer b2=ByteBuffer.wrap("dddd".getBytes());
        byteBuffer.put(b2);
        System.out.println(new String(byteBuffer.array()));

        System.out.println(byteBuffer);
    }

    public void printByteBuffr(ByteBuffer byteBuffer){
        System.out.println(byteBuffer);
    }

    public static void main(String[] args) {
        System.out.println("------ test allocate ------");
        System.out.println(String.format("before allocate:%s",Runtime.getRuntime().freeMemory()));

        ByteBuffer.allocate(102400);
        System.out.println(String.format("after allocate:%s",Runtime.getRuntime().freeMemory()));

        ByteBuffer.allocateDirect(102400);
        System.out.println(String.format("after direct allocate:%s",Runtime.getRuntime().freeMemory()));


        byte[] bytes=new byte[512];
        ByteBuffer bb= ByteBuffer.wrap(bytes);
        //bb.put((byte) 4);
        System.out.println(bb);


        System.out.println(ByteBuffer.wrap(bytes,10,10));
    }
}
