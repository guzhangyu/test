package com.raycloud.dmj.mapper;

import com.raycloud.dmj.entity.User;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long userId);

    List<User> findAll();

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}