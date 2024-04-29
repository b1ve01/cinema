package com.example.demo.mapper;

import com.example.demo.pojo.House;
import com.example.demo.pojo.Movie;
import com.example.demo.pojo.Schedule;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ScheduleMapper {

    //查询表中最大的ID
    @Select("SELECT IFNULL(MAX(schedule_id), 0) FROM schedule")
    long getMaxScheduleId();

    @Insert("INSERT INTO schedule(schedule_id,movie_id,cinema_id,house_id,schedule_time,schedule_price,schedule_remain,schedule_state,schedule_description)" + " VALUES (#{scheduleId},#{movieId},#{cinemaId},#{houseId},#{scheduleTime},#{schedulePrice},#{scheduleRemain},#{scheduleState},#{scheduleDescription})")
    void create(Schedule schedule);

    @Select("select * from schedule where schedule_id = #{scheduleId}")
    Schedule infoById(long scheduleId);

    @Select("select * from schedule where movie_id = #{movieId} and schedule_state=1 order by schedule_time ASC ")
    List<Schedule> infoByMovie(long movieId);

    @Select("select * from schedule where cinema_id = #{cinemaId} and schedule_state=1 order by schedule_time ASC ")
    List<Schedule> infoByCinema(long cinemaId);

    @Select("select * from schedule where movie_id = #{movieId} and cinema_id = #{cinemaId} and schedule_state=1 order by schedule_time ASC ")
    List<Schedule> infoByMovieAndCinema(long movieId,long cinemaId);

    @Delete("delete from schedule where schedule_id = #{scheduleId} ")
    void deleteByScheduleId(long scheduleId);

}
