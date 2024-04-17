package com.example.demo.service;

import com.example.demo.pojo.Cinema;
import com.example.demo.pojo.House;

public interface HouseService {

    //查询表中最大的ID
    long getMaxHouseId();

    void create(House house);

    House infoById(long houseId);

}
