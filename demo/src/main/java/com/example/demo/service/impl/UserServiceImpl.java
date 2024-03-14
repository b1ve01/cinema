package com.example.demo.service.impl;

import com.example.demo.mapper.UserMapper;
import com.example.demo.pojo.User;
import com.example.demo.service.UserService;
import com.example.demo.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    //通过userId查询用户
    @Override
    public User findById(long userId) {
        return userMapper.findById(userId);
    }

    //查询用户表中最大的ID
    @Override
    public long getMaxUserId() {
        return userMapper.getMaxUserId();
    }

    //通过userEmail查询用户
    @Override
    public User findByEmail(String userEmail) {
        return userMapper.findByEmail(userEmail);
    }

    //用户注册
    @Override
    public void register(User user) {
        //加密密码
        String md5String = MD5Utils.MD5Upper(user.getUserPassword());
        user.setUserPassword(md5String);
        //添加
        user.setUserId(getMaxUserId() + 1);
        userMapper.add(user);
        System.out.println("添加成功");
    }

    //更新用户名字，电话和简介信息
    @Override
    public void update(User user) {
        userMapper.update(user);
    }

}
