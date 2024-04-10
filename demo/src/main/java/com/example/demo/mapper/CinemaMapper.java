package com.example.demo.mapper;

import com.example.demo.pojo.Cinema;
import com.example.demo.pojo.Movie;
import com.example.demo.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CinemaMapper {

    //通过cinemaId查询用户
    @Select("select * from cinema where cinema_id = #{cinemaId}")
    Cinema findById(long cinemaId);

    //查询用户表中最大的ID
    @Select("SELECT IFNULL(MAX(cinema_id), 0) FROM cinema")
    long getMaxCinemaId();

    //通过cinemaPhone查询用户
    @Select("select * from cinema where cinema_phone = #{cinemaPhone}")
    Cinema findByPhone(String cinemaPhone);

    //用户注册
    @Insert("INSERT INTO cinema(cinema_id,cinema_phone,cinema_password)" + " VALUES (#{cinemaId},#{cinemaPhone},#{cinemaPassword})")
    void add(Cinema cinema);

    @Select("select * from cinema ")
    List<Cinema> infoAllCinema();

}
