import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * 响应中断事件，并关闭流的socket读线程
 * Created by guzy on 16/6/28.
 */
public class ReaderThread extends Thread {

    final Socket socket;
    final InputStream in;

    public ReaderThread(Socket socket)throws IOException{
        this.socket=socket;
        this.in=socket.getInputStream();
    }

    public void interrupt(){
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            super.interrupt();
        }
    }

    public void run(){
        byte[]buf=new byte[1024];
        try {
            while(true){
                int count= 0;
                count = in.read(buf);
                if(count<=0){
                    break;
                }
                processBuf(buf,count);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void processBuf(byte[] buf, int count) {

    }
}
