package com.raycloud.dmj;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import rx.Observable;
import rx.Observer;
import rx.functions.Action1;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class HelloWorldCommand extends HystrixCommand<String> {

    private final String name;

    public HelloWorldCommand(String name){
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.name=name;
    }


    @Override
    protected String run() throws Exception {
        return "Hello "+name+" thread:"+Thread.currentThread().getName();
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        HelloWorldCommand helloWorldCommand=new HelloWorldCommand("Synchronous-hystrix");
//        String result = helloWorldCommand.execute();
//        System.out.println("result="+result);
//
//        helloWorldCommand = new HelloWorldCommand("Asynchronous-hystrix");
//        Future<String> future=helloWorldCommand.queue();
//        result=future.get(100, TimeUnit.MILLISECONDS);
//        System.out.println("result="+result);
//        System.out.println("mainThread="+Thread.currentThread().getName());

        Observable<String> fs=helloWorldCommand.observe();
        Action1<String> action1=new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println("on call:"+s);
            }
        };
        for (int i=0;i<100;i++){
            Thread.sleep(1000l);
            fs.subscribe(action1);
        }
        fs.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                System.out.println("on complete");
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("on error:"+throwable);
                throwable.printStackTrace();
            }

            @Override
            public void onNext(String s) {
                System.out.println("on next:"+s);
            }
        });


    }
}
