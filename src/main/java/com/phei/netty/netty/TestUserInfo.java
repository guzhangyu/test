package com.phei.netty.netty;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * 测试byteBuffer的速度
 * Created by guzy on 16/8/1.
 */
public class TestUserInfo {

    public static void main(String[] args) throws IOException {
        UserInfo userInfo=new UserInfo();
        userInfo.setUserId(22);
        userInfo.setUserName("gu zhangyu ");

        int loop=100000;

        long begin=System.currentTimeMillis();
        for(int i=0;i<loop;i++){
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            ObjectOutputStream os=new ObjectOutputStream(baos);
            os.writeObject(userInfo);
            os.flush();
            os.close();
            baos.close();
        }
        long end=System.currentTimeMillis()-begin;
        begin=System.currentTimeMillis();

       for(int i=0;i<loop;i++){
           userInfo.codeC();
       }
        System.out.println(String.format("use time compare,java:%d,netty:%d",end,(System.currentTimeMillis()-begin)));

     //   System.out.println(String.format("java ser size:%d,netty size:%d",baos.toByteArray().length,userInfo.codeC().length));


    }
}
