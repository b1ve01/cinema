package com.example.demo.service.impl;

import com.example.demo.mapper.UserMapper;
import com.example.demo.pojo.User;
import com.example.demo.service.UserService;
import com.example.demo.utils.MD5Util;
import com.example.demo.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

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
        String md5String = MD5Util.MD5Upper(user.getUserPassword());
        user.setUserPassword(md5String);
        //添加
        user.setUserId(getMaxUserId() + 1);
        userMapper.add(user);
        System.out.println("添加成功");
    }

    //更新userName，userPhone和userProfile
    @Override
    public void update(User user) {
        userMapper.update(user);
    }

    //更新userUrl
    @Override
    public void updateUrl(User user){userMapper.updateUrl(user);}

    //更新userPassword（记得原密码）
    @Override
    public void updatePassword(String new_password){
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer temp_userId = (Integer) map.get("userId");
        long userId=temp_userId.longValue();
        userMapper.updatePassword(MD5Util.MD5Upper(new_password),userId);
    }

    //更新userPasswordByUserEmail
    @Override
    public void updatePasswordByEmail(User user){
        String md5String = MD5Util.MD5Upper(user.getUserPassword());
        user.setUserPassword(md5String);
        userMapper.updatePasswordByEmail(user);
    }

    //更新userEmail
    @Override
    public void updateEmail(User user){userMapper.updateEmail(user);}

}
