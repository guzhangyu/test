package com.test.config;

import org.springframework.stereotype.Component;

@Component
public class TestAop{
    public void playTrack(int trackNumber){
        System.out.println("haha");
    }
}
