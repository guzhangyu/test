package com.test;

//import com.test.config.Test1;
import com.test.config.TestAop;
import com.test.config.TestConfig;
import com.test.db.Item;
import com.test.db.Order;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ServletComponentScan
@ComponentScan("com.test")
public class SpringAssembly {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext=SpringApplication.run(SpringAssembly.class,args);

//        RedisExample example=applicationContext.getBean(RedisExample.class);
//        List list=example.testWatch();
//        System.out.println(list);
//        System.out.println(((TestConfig)applicationContext.getBean("testConfig")).m);
//        assert ((TestConfig)applicationContext.getBean("testConfig")).test!=null;

//        ((TestAop)applicationContext.getBean("testAop")).playTrack(3);
//        RedisLock redisLock=example.lock(0,"gzy",6000l,6000l);
//        System.out.println(redisLock);
//
//        System.out.println(example.unLock(0,redisLock));
//
//        redisLock=example.lock(0,"gzy",6000l,6000l);
//        System.out.println(redisLock);

//        MongoOperations mongo=(MongoOperations) applicationContext.getBean("mongoTemplate");
//        long orderCount = mongo.getCollection("order").count();
//        System.out.println(orderCount);
//        Order order=new Order();
//        order.setCustomer("ADD");
//        order.setType("a");
//        order.setId("abc123");
//        order.setItems(Arrays.asList(
//                new Item(123l,null,"test_prod",4d,4),
//                new Item(124l,null,"test_prod",4d,4)
//        ));
//        mongo.save(order,"order");
//
//        order=mongo.findById("abc123",Order.class);
//        System.out.println(order.getItems());
//        Collections.synchronized

//        RedisTemplate<String,String> redisTemplate=(RedisTemplate<String,String>)applicationContext.getBean("redisTemplate");
//        ValueOperations<String,String> valueOperations=redisTemplate.opsForValue();
//        valueOperations.set("da","hdes");
//        System.out.println(valueOperations.get("da"));

        RabbitTemplate rabbitTemplate=applicationContext.getBean(RabbitTemplate.class);
        rabbitTemplate.convertAndSend("unitymob", "testQueue", (Object) "dafdsa".getBytes());
    }
}
