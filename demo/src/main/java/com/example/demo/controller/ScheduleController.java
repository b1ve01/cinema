package com.example.demo.controller;

import com.example.demo.pojo.House;
import com.example.demo.pojo.Movie;
import com.example.demo.pojo.Result;
import com.example.demo.pojo.Schedule;
import com.example.demo.service.HouseService;
import com.example.demo.service.ScheduleService;
import com.example.demo.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/create")
    public Result<Schedule> create(@RequestBody Schedule schedule){
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer temp_cinemaId = (Integer) map.get("cinemaId");
        long cinemaId=temp_cinemaId.longValue();
        schedule.setCinemaId(cinemaId);
        scheduleService.create(schedule);
        return Result.success(schedule);
    }

    @GetMapping("/infoByMovie")
    public Result<List<Schedule>> infoByMovie(long movieId){
        List<Schedule> res=scheduleService.infoByMovie(movieId);
        return Result.success(res);
    }

    @GetMapping("/infoByMovieAndCinema")
    public Result<List<Schedule>> infoByMovieAndCinema(long movieId,long cinemaId){
        List<Schedule> res=scheduleService.infoByMovieAndCinema(movieId,cinemaId);
        return Result.success(res);
    }

}
