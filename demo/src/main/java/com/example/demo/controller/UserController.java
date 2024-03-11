package com.example.demo.controller;

import com.example.demo.pojo.Result;
import com.example.demo.pojo.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    //用户注册
    @PostMapping("/register")
    public Result<User> register(@RequestBody User user){
        //查询用户
        User temp_u = userService.findByPhone(user.getUserPhone());
        if (temp_u == null){
            //注册
            userService.register(user);
            return Result.success(user);
        }else{
            return Result.error("用户已存在");
        }
    }

}