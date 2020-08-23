package com.raycloud.dmj;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

public class RequestCacheCommand extends HystrixCommand<String> {

    private final int id;

    protected RequestCacheCommand(int id) {
        super(HystrixCommandGroupKey.Factory.asKey("RequestCacheCommand"));
        this.id=id;
    }

    @Override
    protected String run() throws Exception {
        System.out.println(Thread.currentThread().getName()+" execute id="+id);
        return "executed="+id;
    }

    @Override
    protected String getCacheKey() {
        return String.valueOf(id);
    }

    public static void main(String[] args) {
        HystrixRequestContext context=HystrixRequestContext.initializeContext();

        try{
            RequestCacheCommand command2a=new RequestCacheCommand(2);
            RequestCacheCommand command2b=new RequestCacheCommand(2);
            System.out.println(command2a.execute());
            System.out.println(command2a.isResponseFromCache());
            assert command2a.isResponseFromCache();
            System.out.println(command2b.execute());
            System.out.println(command2b.isResponseFromCache());
            assert command2b.isResponseFromCache();
        }finally {
            context.shutdown();
        }

        context=HystrixRequestContext.initializeContext();
        try{
            RequestCacheCommand command2c=new RequestCacheCommand(2);
            System.out.println(command2c.execute());
            assert command2c.isResponseFromCache();
        }finally {
            context.shutdown();
        }
    }
}
