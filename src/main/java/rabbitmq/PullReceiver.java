package rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class PullReceiver {

    final static String QUEUE_NAME="MyQueue";

    public static void main(String[] args) {
        receive();
    }

    private static void receive() {
        Connection connection=null;
        Channel channel=null;
        try{
            ConnectionFactory factory=new ConnectionFactory();
            factory.setHost("localhost");
            connection=factory.newConnection();
            channel=connection.createChannel();
            //channel.queueDeclare(QUEUE_NAME,false,false,false,null);

            //这是异步处理的方法，所以后面要wait
            long count=channel.messageCount(QUEUE_NAME);
            for(long i=0;i<count;i++){
                GetResponse response=channel.basicGet(QUEUE_NAME,true);
                System.out.println(String.format("收到消息.....%s",new String(response.getBody(),"UTF-8")));
            }

        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(channel!=null){
                try {
                    channel.close();
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
