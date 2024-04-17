package com.example.demo.mapper;

import com.example.demo.pojo.Cinema;
import com.example.demo.pojo.House;
import com.example.demo.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface HouseMapper {

    //查询表中最大的ID
    @Select("SELECT IFNULL(MAX(house_id), 0) FROM house")
    long getMaxHouseId();

    @Insert("INSERT INTO house(house_id,house_name,house_seats,cinema_id)" + " VALUES (#{houseId},#{houseName},#{houseSeats},#{cinemaId})")
    void create(House house);

    @Select("select * from house where house_id = #{houseId}")
    House infoById(long houseId);

}
