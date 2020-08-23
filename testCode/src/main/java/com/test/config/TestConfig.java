package com.test.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class TestConfig {

    @Autowired
    @Cold
    public Test test;

    public List<TestBean> testBeans=new ArrayList<>();

    public List<String> strs=new ArrayList<>();

    public TestConfig(){
        testBeans.add(new TestBean("a"));
        testBeans.add(new TestBean("b"));

        strs.add("a");
        strs.add("b");
    }

//    @Value("#{testConfig.strs.?[_ eq 'a'][0]}")
    public String m;
}

@Component
class TestBean{

    public String a=new Random().nextInt(100)+"";

    List<String> strs=new ArrayList<>();

    public TestBean(){
        for(int i=0;i<3;i++){
            strs.add(i+"");
        }
    }

    public TestBean(String a){
        this.a=a;
    }
}

interface Test{

}

@Retention(RetentionPolicy.RUNTIME)
@Qualifier
@interface Cold{ }

@Component
class Test1 implements Test{

}

@Cold
@Component
class Test2 implements Test{

}

