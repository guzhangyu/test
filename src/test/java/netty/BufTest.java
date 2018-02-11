package netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import org.junit.Test;

/**
 * Created by guzy on 16/9/24.
 */
public class BufTest {

    final int loop=3000000;

    final byte[] CONTENT="hello test".getBytes();

    public void testPool(){

        long startTime=System.currentTimeMillis();
        ByteBuf poolBuffer = null;
        for(int i=0;i<loop;i++){
            poolBuffer= PooledByteBufAllocator.DEFAULT.directBuffer(1024);
            poolBuffer.writeBytes(CONTENT);
            poolBuffer.release();
        }
        System.out.println(String.format("pooled use time:%d",System.currentTimeMillis()-startTime));
    }

    public void testUnPooled(){
        long startTime=System.currentTimeMillis();
        ByteBuf buffer=null;
        for(int i=0;i<loop;i++){
            buffer= Unpooled.directBuffer(1024);
            buffer.writeBytes(CONTENT);
        }
        System.out.println(String.format("unpooled use time:%d",System.currentTimeMillis()-startTime));
    }

    @Test
    public void comparePool(){
        testPool();
        testUnPooled();
        testUnPooled();
        testPool();
    }


}
