package com.test.config;

import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

@Component
public class RabbitmqListener {

    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(value=@Queue(value="testQueue",durable = "true"),key = "testQueue",exchange = @Exchange(value="unitymob",durable = "true")))
    public void listen(byte[] message){
        System.out.println(new String(message));
    }
}
