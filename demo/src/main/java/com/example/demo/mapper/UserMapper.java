package com.example.demo.mapper;

import com.example.demo.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {

    //通过userId查询用户
    @Select("select * from user where user_id = #{userId}")
    User findById(long userId);

    //查询用户表中最大的ID
    @Select("SELECT IFNULL(MAX(user_id), 0) FROM user")
    long getMaxUserId();

    //通过userEmail查询用户
    @Select("select * from user where user_email = #{userEmail}")
    User findByEmail(String userEmail);

    //用户注册
    @Insert("INSERT INTO user(user_id,user_email,user_password)" + " VALUES (#{userId},#{userEmail},#{userPassword})")
    void add(User user);

    //更新userName，userPhone和userProfile
    @Update("update user set user_name=#{userName},user_phone=#{userPhone},user_profile=#{userProfile} where user_id = #{userId}")
    void update(User user);

    //更新userUrl
    @Update("update user set user_url=#{userUrl} where user_id = #{userId}")
    void updateUserUrl(User user);

    //更新userPassword（记得原密码）
    @Update("update user set user_password = #{new_password} where user_id = #{userId}")
    void updateUserPassword(String new_password, long userId);

}
