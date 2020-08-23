package com.raycloud.dmj;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;

import java.util.concurrent.TimeUnit;

public class HelloWorldCommandFallback extends HystrixCommand<String> {

    private final String name;

    public HelloWorldCommandFallback(String name) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("HelloWorldGroup"))
        .andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionIsolationThreadTimeoutInMilliseconds(500)));
        this.name = name;
    }

    @Override
    protected String getFallback() {
        return "execute Failed";
    }

    @Override
    protected String run() throws Exception {
        TimeUnit.MILLISECONDS.sleep(1000);
        return "Hello "+name+" thread:"+Thread.currentThread().getName();
    }

    public static void main(String[] args) {
        HelloWorldCommandFallback command=new HelloWorldCommandFallback("test-Fallback");
        System.out.println(command.execute());
    }
}
