package com.example.demo.service;

import com.example.demo.pojo.Cinema;
import com.example.demo.pojo.House;
import com.example.demo.pojo.Movie;
import com.example.demo.pojo.User;

import java.util.List;

public interface CinemaService {

    //通过cinemaId查询用户
    Cinema findById(long cinemaId);

    //查询用户表中最大的ID
    long getMaxCinemaId();

    //通过cinemaPhone查询用户
    Cinema findByPhone(String cinemaPhone);

    //用户注册
    void register(Cinema cinema);

    List<Cinema> infoAllCinema();

    Cinema infoById(long cinemaId);

    void deleteByCinemaId(long cinemaId);

    void update(Cinema cinema);

}
