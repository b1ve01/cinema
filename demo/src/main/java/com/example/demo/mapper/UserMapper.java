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
    @Select("select * from user where user_phone = #{userPhone}")
    User findByPhone(String userPhone);

    //用户注册
    @Insert("INSERT INTO user(user_id,user_phone,user_password)" +
            " VALUES (#{userId},#{userPhone}, #{userPassword})")
    void add(User user);

}
