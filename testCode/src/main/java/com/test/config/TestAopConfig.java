package com.test.config;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class TestAopConfig {

    @Pointcut("execution(* com.test.config.TestAop.playTrack(int)) && args(trackNumber)")
    public void trackPlayed(int trackNumber){}

    @Before("trackPlayed(trackNumber)")
    public void countTrack(int trackNumber){
        System.out.println(trackNumber);
    }

    @Autowired
    Testt testt;

    @After("execution(* com.test.config.TestAop.playTrack(..))")
    public void after(){
        testt.a();
        System.out.println("after");
    }
}

@Component
class Testt{

    public void a(){
        System.out.println("a");
    }
}
