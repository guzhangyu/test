package rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Sender {
    
    final static String QUEUE_NAME="MyQueue";


    public static void main(String[] args) {
        send();
    }

    private static void send() {
        Connection connection=null;
        Channel channel=null;

        try{
            ConnectionFactory factory=new ConnectionFactory();
            factory.setHost("localhost");
            connection=factory.newConnection();
            channel=connection.createChannel();
            //channel.queueDeclare(QUEUE_NAME,false,false,false,null);

            String message="my first message .....";
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes("UTF-8"));
            System.out.println("已经发送消息....."+message);

            channel.basicPublish("",QUEUE_NAME,null,message.getBytes("UTF-8"));
            System.out.println("已经发送消息....."+message);

            channel.basicPublish("",QUEUE_NAME,null,message.getBytes("UTF-8"));
            System.out.println("已经发送消息....."+message);


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
