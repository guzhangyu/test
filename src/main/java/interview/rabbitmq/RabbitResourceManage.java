package interview.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public abstract  class RabbitResourceManage {

    final static String QUEUE_NAME="MyQueue";
    Channel channel;
    Connection connection;
    ConnectionFactory factory=new ConnectionFactory();

    public RabbitResourceManage() {
        factory.setHost("localhost");
    }

    public void operateRabbit() {
        try{
            initChannel();
            operate();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            close();
        }
    }

    protected abstract void operate() throws IOException, TimeoutException;

    protected void close() {
        try {
            if(channel!=null && channel.isOpen()){
                channel.close();
            }
            if(connection!=null && connection.isOpen()){
                connection.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    protected Channel initChannel() throws IOException, TimeoutException {
        if(channel==null || !channel.isOpen()){
            if(connection==null || !connection.isOpen()){
                connection=factory.newConnection();
            }
            channel=connection.createChannel();
            channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        }
        return channel;
    }
}
