package interview.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class SubscribeReceiver {

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
            channel.queueDeclare(QUEUE_NAME,false,false,false,null);

            //这是异步处理的方法，所以后面要wait
            channel.basicConsume(QUEUE_NAME,true,new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    System.out.println(String.format("收到消息.....%s",new String(body,"UTF-8")));
                }
            });

            synchronized (SubscribeReceiver.class){
                try {
                    SubscribeReceiver.class.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
