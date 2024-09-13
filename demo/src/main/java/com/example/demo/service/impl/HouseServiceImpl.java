package com.example.demo.service.impl;

import com.example.demo.mapper.CinemaMapper;
import com.example.demo.mapper.HouseMapper;
import com.example.demo.pojo.Cinema;
import com.example.demo.pojo.House;
import com.example.demo.pojo.User;
import com.example.demo.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HouseServiceImpl implements HouseService {

    @Autowired
    private HouseMapper houseMapper;

    //查询表中最大的ID
    @Override
    public long getMaxHouseId() {return houseMapper.getMaxHouseId();}

    @Override
    public void create(House house) {
        house.setHouseId(getMaxHouseId() + 1);
        houseMapper.create(house);
    }

    @Override
    public House infoById(long houseId){return houseMapper.infoById(houseId);}

    @Override
    public List<House> infoByCinemaId(long cinemaId){return houseMapper.infoByCinemaId(cinemaId);}

    @Override
    public void deleteByHouseId(long houseId){houseMapper.deleteByHouseId(houseId);}

    @Override
    public void update(House house) {
        houseMapper.update(house);
    }

}
