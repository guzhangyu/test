package interview.rabbitmq;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Sender extends RabbitResourceManage{
    
    final static String QUEUE_NAME="MyQueue";

    private static final Logger logger= Logger.getLogger(Sender.class);


    public static void main(String[] args) {
        new Sender().operateRabbit();
    }

    @Override
    protected void operate() throws IOException, TimeoutException {
        String message="my first message .....";
        channel.basicPublish("",QUEUE_NAME,null,message.getBytes("UTF-8"));
        System.out.println("已经发送消息....."+message);

        channel.basicPublish("",QUEUE_NAME,null,message.getBytes("UTF-8"));
        System.out.println("已经发送消息....."+message);

        channel.basicPublish("",QUEUE_NAME,null,message.getBytes("UTF-8"));
        System.out.println("已经发送消息....."+message);
    }
}
