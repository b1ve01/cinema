package com.example.demo.service;

import com.example.demo.pojo.User;

public interface UserService {

    //查询用户表中最大的ID
    Long getMaxUserId();

    //通过电话号码查询用户
    User findByEmail(String userEmail);

    //用户注册
    void register(User user);

}
