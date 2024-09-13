package com.example.demo.mapper;

import com.example.demo.pojo.Cinema;
import com.example.demo.pojo.House;
import com.example.demo.pojo.Movie;
import com.example.demo.pojo.User;
import org.apache.ibatis.annotations.*;

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
    @Insert("INSERT INTO cinema(cinema_id,cinema_name,cinema_phone,cinema_password,cinema_address,cinema_x,cinema_y)" + " VALUES (#{cinemaId},#{cinemaName},#{cinemaPhone},#{cinemaPassword},#{cinemaAddress},#{cinemaX},#{cinemaY})")
    void add(Cinema cinema);

    @Select("select * from cinema where cinema_id>0 ")
    List<Cinema> infoAllCinema();

    @Select("select * from cinema where cinema_id = #{cinemaId}")
    Cinema infoById(long cinemaId);

    @Delete("delete from cinema where cinema_id = #{cinemaId} ")
    void deleteByCinemaId(long cinemaId);

    @Update("update cinema set cinema_name=#{cinemaName},cinema_phone=#{cinemaPhone},cinema_password=#{cinemaPassword},cinema_address=#{cinemaAddress},cinema_x=#{cinemaX},cinema_y=#{cinemaY} where cinema_id = #{cinemaId}")
    void update(Cinema cinema);

}
