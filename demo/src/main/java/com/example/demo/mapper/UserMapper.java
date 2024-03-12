package com.example.demo.mapper;

import com.example.demo.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    //查询用户表中最大的ID
    @Select("SELECT IFNULL(MAX(user_id), 0) FROM user")
    Long getMaxUserId();

    //通过电话号码查询用户
    @Select("select * from user where user_email = #{userEmail}")
    User findByEmail(String userEmail);

    //用户注册
    @Insert("INSERT INTO user(user_id,user_email,user_password)" + " VALUES (#{userId},#{userEmail},#{userPassword})")
    void add(User user);

}
