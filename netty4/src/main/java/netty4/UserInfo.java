package netty4;

import java.io.Serializable;
import java.nio.ByteBuffer;

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

    public byte[] codeC(){
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        byte[] value = this.getUserName().getBytes();
        buffer.putInt(value.length);
        buffer.put(value);

        buffer.putInt(this.getUserId());
        buffer.flip();
        value = null;

        byte[] result = new byte[buffer.remaining()];
        buffer.get(result);
        return result;
    }
}
