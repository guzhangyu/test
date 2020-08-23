package com.raycloud.dmj.service;

import com.raycloud.dmj.entity.User;
import com.raycloud.dmj.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DemoService {

    @Resource
    UserMapper userMapper;

    public static Long userId=190l;

    public void demo(){
        System.out.println("Insert ......");
        for(int i=1;i<100;i++){
            User user=new User();
            user.setUserId(userId);
            user.setAccount("account"+i);
            user.setCityId((long)i);
            user.setPassword("pass"+i);
            user.setUserName("name"+i);
            userId++;

            userMapper.insert(user);
        }
        System.out.println("over........");
    }

    public User getUserByUserId(Long id){
        return userMapper.selectByPrimaryKey(id);
    }

    public List<User> findAll(){
        return userMapper.findAll();
    }
}
