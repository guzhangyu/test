package interview.rabbitmq;

import com.rabbitmq.client.GetResponse;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class PullReceiver extends RabbitResourceManage{

    public static void main(String[] args) {
        new PullReceiver().operateRabbit();
    }


    @Override
    protected void operate() throws IOException, TimeoutException {
        //这是异步处理的方法，所以后面要wait
        long count=channel.messageCount(QUEUE_NAME);
        for(long i=0;i<count;i++){
            GetResponse response=channel.basicGet(QUEUE_NAME,true);
            System.out.println(String.format("收到消息.....%s",new String(response.getBody(),"UTF-8")));
        }
    }
}
