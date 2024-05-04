package com.example.demo.service;

import com.example.demo.pojo.House;
import com.example.demo.pojo.Movie;
import com.example.demo.pojo.Schedule;

import java.util.List;

public interface ScheduleService {

    //查询表中最大的ID
    long getMaxScheduleId();

    void create(Schedule schedule);

    Schedule infoById(long scheduleId);

    List<Schedule> infoByMovie(long movieId);

    List<Schedule> infoByCinema(long cinemaId);

    List<Schedule> infoByMovieAndCinema(long movieId,long cinemaId);

    void deleteByScheduleId(long scheduleId);

    void update(Schedule schedule);

}
