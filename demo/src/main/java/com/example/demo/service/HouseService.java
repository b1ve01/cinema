package com.example.demo.service;

import com.example.demo.pojo.Cinema;
import com.example.demo.pojo.House;
import com.example.demo.pojo.User;

import java.util.List;

public interface HouseService {

    //查询表中最大的ID
    long getMaxHouseId();

    void create(House house);

    House infoById(long houseId);

    List<House> infoByCinemaId(long cinemaId);

    void deleteByHouseId(long houseId);

    void update(House house);

}
