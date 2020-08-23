package com.raycloud.dmj;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;

public class HelloWorldCommandDependency extends HystrixCommand<String> {

    private final String name;

    public HelloWorldCommandDependency(String name) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleDependency"))
        .andCommandKey(HystrixCommandKey.Factory.asKey("HelloWorld")));
        this.name = name;
    }

    @Override
    protected String run() throws Exception {
        return "hahaha";
    }



    public static void main(String[] args) {
//        HelloWorldCommand helloWorldCommand=new HelloWorldCommand("Synchronous-hystrix");
        HelloWorldCommandDependency dependency=new HelloWorldCommandDependency("nihao");
        System.out.println(dependency.execute());
        System.out.println(dependency.getCommandKey());
    }
}
