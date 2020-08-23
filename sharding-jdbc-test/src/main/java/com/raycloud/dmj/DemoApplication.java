package com.raycloud.dmj;

import com.raycloud.dmj.entity.User;
import com.raycloud.dmj.service.DemoService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@EnableTransactionManagement(proxyTargetClass = true)
public class DemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext=SpringApplication.run(DemoApplication.class,args);
        DemoService demoService=applicationContext.getBean(DemoService.class);
//        demoService.demo();
//        User user=demoService.getUserByUserId(191l);
//        System.out.println(user.getAccount());
        List<User> users=demoService.findAll();
        System.out.println(users);
    }
}
