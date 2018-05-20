package interview.rabbitmq;

import com.rabbitmq.client.GetResponse;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeoutException;

public class ManualAckPullReceiver extends RabbitResourceManage{

    public static void main(String[] args) {
        new ManualAckPullReceiver().operateRabbit();
    }

    @Override
    protected void operate() throws IOException, TimeoutException {
        while(true){
            GetResponse response=channel.basicGet(QUEUE_NAME,false);
            try{
                if(response==null || response.getBody()==null){
                    break;
                }
                if(new Random().nextInt(100)>40){
                    throw new IllegalArgumentException("haha");
                }
                System.out.println(String.format("收到消息.....%s",new String(response.getBody(),"UTF-8")));
                channel.basicAck(response.getEnvelope().getDeliveryTag(),false);
            }catch (Exception e){
                e.printStackTrace();
                channel.abort();
            }finally {
                initChannel();
            }
        }
    }
}
