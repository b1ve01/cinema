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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    public Result<Schedule> create(@RequestBody Schedule schedule,String scheduleTime){
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer temp_cinemaId = (Integer) map.get("cinemaId");
        long cinemaId=temp_cinemaId.longValue();
        schedule.setCinemaId(cinemaId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(scheduleTime, formatter);

        schedule.setScheduleTime(dateTime);
        scheduleService.create(schedule);
        return Result.success(schedule);
    }

    @GetMapping("/infoById")
    public Result<Schedule> infoById(long scheduleId){
        Schedule temp_schedule=scheduleService.infoById(scheduleId);
        return Result.success(temp_schedule);
    }

    @GetMapping("/infoByMovie")
    public Result<List<Schedule>> infoByMovie(long movieId){
        List<Schedule> res=scheduleService.infoByMovie(movieId);
        return Result.success(res);
    }

    @GetMapping("/infoByCinema")
    public Result<List<Schedule>> infoByCinema(long cinemaId){
        List<Schedule> res=scheduleService.infoByCinema(cinemaId);
        return Result.success(res);
    }

    @GetMapping("/infoByMovieAndCinema")
    public Result<List<Schedule>> infoByMovieAndCinema(long movieId,long cinemaId){
        List<Schedule> res=scheduleService.infoByMovieAndCinema(movieId,cinemaId);
        return Result.success(res);
    }

    @DeleteMapping("/deleteByScheduleId")
    public Result<Schedule> deleteByScheduleId(long scheduleId){
        scheduleService.deleteByScheduleId(scheduleId);
        return Result.success();
    }

    @PutMapping("/update")
    public Result<Schedule> update(@RequestBody Schedule schedule,String scheduleTime){
        System.out.println(scheduleTime);
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer temp_cinemaId = (Integer) map.get("cinemaId");
        long cinemaId=temp_cinemaId.longValue();
        schedule.setCinemaId(cinemaId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(scheduleTime, formatter);

        schedule.setScheduleTime(dateTime);
        scheduleService.update(schedule);
        return Result.success(schedule);
    }

}
