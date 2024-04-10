package com.example.demo.service.impl;

import com.example.demo.mapper.CinemaMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.pojo.Cinema;
import com.example.demo.pojo.Movie;
import com.example.demo.pojo.User;
import com.example.demo.service.CinemaService;
import com.example.demo.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CinemaServiceImpl implements CinemaService {

    @Autowired
    private CinemaMapper cinemaMapper;

    //通过cinemaId查询用户
    @Override
    public Cinema findById(long cinemaId) {
        return cinemaMapper.findById(cinemaId);
    }

    //查询用户表中最大的ID
    @Override
    public long getMaxCinemaId() {
        return cinemaMapper.getMaxCinemaId();
    }

    //通过cinemaPhone查询用户
    @Override
    public Cinema findByPhone(String cinemaPhone) {
        return cinemaMapper.findByPhone(cinemaPhone);
    }

    //用户注册
    @Override
    public void register(Cinema cinema) {
        //加密密码
        String md5String = MD5Util.MD5Upper(cinema.getCinemaPassword());
        cinema.setCinemaPassword(md5String);
        //添加
        cinema.setCinemaId(getMaxCinemaId() + 1);
        cinemaMapper.add(cinema);
        System.out.println("添加成功");
    }

    @Override
    public List<Cinema> infoAllCinema(){return cinemaMapper.infoAllCinema();}

}
