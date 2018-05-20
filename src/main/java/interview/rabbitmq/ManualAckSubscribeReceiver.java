package interview.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeoutException;

public class ManualAckSubscribeReceiver extends RabbitResourceManage{

    public static void main(String[] args) {

        new ManualAckSubscribeReceiver().operateRabbit();
    }

    @Override
    protected void operate() throws IOException, TimeoutException {
        channel.basicQos(1);
        //这是异步处理的方法，所以后面要wait
        channel.basicConsume(QUEUE_NAME,false,new ManualAckConsumer(channel));

        synchronized (ManualAckSubscribeReceiver.class){
            try {
                ManualAckSubscribeReceiver.class.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    class ManualAckConsumer extends DefaultConsumer{
        /**
         * Constructs a new instance and records its association to the passed-in channel.
         *
         * @param channel the channel to which this consumer is attached
         */
        public ManualAckConsumer(Channel channel) {
            super(channel);
        }

        @Override
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
            try{
                if(new Random().nextInt(100)>40){
                    throw new IllegalArgumentException("haha");
                }
                System.out.println(String.format("收到消息.....%s",new String(body,"UTF-8")));
                channel.basicAck(envelope.getDeliveryTag(),false);
            }catch (Exception e){
                e.printStackTrace();
                channel.abort();
            }finally {
                //保证一定要执行成功
                boolean success=false;
                while(!success){
                    try{
                        initChannel();
                        channel.basicQos(1);
                        if(channel.messageCount(QUEUE_NAME)>0){
                            channel.basicConsume(QUEUE_NAME,false,this);
                        }
                        success=true;
                    }catch (Exception e){

                    }
                }
            }
        }
    }


}
