package com.example.demo.service.impl;

import com.example.demo.mapper.HouseMapper;
import com.example.demo.mapper.ScheduleMapper;
import com.example.demo.pojo.House;
import com.example.demo.pojo.Movie;
import com.example.demo.pojo.Schedule;
import com.example.demo.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleMapper scheduleMapper;

    //查询表中最大的ID
    @Override
    public long getMaxScheduleId() {return scheduleMapper.getMaxScheduleId();}

    @Override
    public void create(Schedule schedule) {
        schedule.setScheduleId(getMaxScheduleId() + 1);
        scheduleMapper.create(schedule);
    }

    @Override
    public Schedule infoById(long scheduleId){return scheduleMapper.infoById(scheduleId);}

    @Override
    public List<Schedule> infoByMovie(long movieId){return scheduleMapper.infoByMovie(movieId);}

    @Override
    public List<Schedule> infoByCinema(long cinemaId){return scheduleMapper.infoByCinema(cinemaId);}

    @Override
    public List<Schedule> infoByMovieAndCinema(long movieId,long cinemaId){return scheduleMapper.infoByMovieAndCinema(movieId,cinemaId);}

    @Override
    public void deleteByScheduleId(long scheduleId){scheduleMapper.deleteByScheduleId(scheduleId);}

}
