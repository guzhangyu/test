package com.phei.netty.file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by guzy on 16/8/13.
 */
public class FileChannelTest {

    public static void main(String[] args) {
        try {
            RandomAccessFile billFile=new RandomAccessFile("/Users/guzy/test.bill","rw");
            FileChannel fileChannel=billFile.getChannel();

            ByteBuffer buffer=ByteBuffer.allocate(1024);
            buffer.put("哈哈".getBytes());
            buffer.flip();

            fileChannel.write(buffer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
