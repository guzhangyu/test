package interview.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class SubscribeReceiver extends RabbitResourceManage{

    public static void main(String[] args) {
        new SubscribeReceiver().operateRabbit();
    }

    @Override
    protected void operate() throws IOException, TimeoutException {
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
    }
}
