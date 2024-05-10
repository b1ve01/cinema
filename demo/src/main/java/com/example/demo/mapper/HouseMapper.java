package com.example.demo.mapper;

import com.example.demo.pojo.Cinema;
import com.example.demo.pojo.House;
import com.example.demo.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface HouseMapper {

    //查询表中最大的ID
    @Select("SELECT IFNULL(MAX(house_id), 0) FROM house")
    long getMaxHouseId();

    @Insert("INSERT INTO house(house_id,house_name,cinema_id,house_x,house_y)" + " VALUES (#{houseId},#{houseName},#{cinemaId},#{houseX},#{houseY})")
    void create(House house);

    @Select("select * from house where house_id = #{houseId}")
    House infoById(long houseId);

    @Select("select * from house where cinema_id = #{cinemaId}")
    List<House> infoByCinemaId(long cinemaId);

    @Delete("delete from house where house_id = #{houseId} ")
    void deleteByHouseId(long houseId);

    @Update("update house set house_name=#{houseName},house_x=#{houseX},house_y=#{houseY} where house_id = #{houseId}")
    void update(House house);

}
