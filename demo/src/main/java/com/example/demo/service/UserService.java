package com.example.demo.service;

import com.example.demo.pojo.User;

public interface UserService {

    //通过userId查询用户
    User findById(long userId);

    //查询用户表中最大的ID
    long getMaxUserId();

    //通过userEmail查询用户
    User findByEmail(String userEmail);

    //用户注册
    void register(User user);

    //更新userName，userPhone和userProfile
    void update(User user);

    //更新userUrl
    void updateUserUrl(User user);

    //更新userPassword（记得原密码）
    void updateUserPassword(String new_password);

}
