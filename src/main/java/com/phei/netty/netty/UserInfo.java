package com.phei.netty.netty;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * Created by guzy on 16/8/1.
 */
public class UserInfo implements Serializable {

    private String userName;

    private int userId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * 将用户的数据转为字节数组
     * @return
     */
    public byte[] codeC(){
        ByteBuffer buffer= ByteBuffer.allocate(1024);
        byte[] value=this.getUserName().getBytes();
        buffer.putInt(value.length);
        buffer.put(value);
        buffer.putInt(this.getUserId());
        buffer.flip();
        value=null;
        byte[] result=new byte[buffer.remaining()];
        buffer.get(result);
        return result;
    }
}
