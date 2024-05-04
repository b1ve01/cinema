package com.example.demo.controller;


import com.example.demo.pojo.*;
import com.example.demo.service.MovieService;
import com.example.demo.service.UserService;
import com.example.demo.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/movie")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/create")
    public Result<Movie> create(@RequestBody Movie movie,String movieReleaseDate,long[] movieType){
        System.out.println(movieReleaseDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(movieReleaseDate, formatter);
        LocalDateTime dateTime = date.atStartOfDay();
        movie.setMovieReleaseDate(dateTime);
        movieService.create(movie);

        long[] temp_arr=movieType;
        for(int i=0;i<temp_arr.length;i++){
            movieService.createType(movie.getMovieId(),temp_arr[i]);
        }

        return Result.success(movie);
    }

    //查询所有电影
    @GetMapping("/info")
    public Result<List<Movie>> info(){
        List<Movie> res=movieService.info();
        return Result.success(res);
    }

    //通过电影id查询电影信息
    @GetMapping("/infoMovieById")
    public Result<Map<String, Object>> infoMovieById(long movieId){
        Movie temp_movie=movieService.infoMovieById(movieId);
        List<MovieGenres> type=movieService.infoTypeByMovie(movieId);

        Map<String, Object> res = new HashMap<>();
        res.put("movie", temp_movie);
        res.put("type", type);

        return Result.success(res);
    }

    //通过电影查询电影类型
    @GetMapping("/infoTypeByMovie")
    public Result<List<MovieGenres>> infoTypeByMovie(long movieId){
        List<MovieGenres> res=movieService.infoTypeByMovie(movieId);
        return Result.success(res);
    }

    //根据状态查询电影
    @GetMapping("/infoMovieByState")
    public Result<List<Movie>> infoMovieByState(String movieState){
        List<Movie> res=movieService.infoMovieByState(movieState);
        return Result.success(res);
    }

    //删除电影
    @DeleteMapping("/deleteByMovieId")
    public Result<Movie> deleteByMovieId(long movieId){
        movieService.deleteByMovieId(movieId);
        return Result.success();
    }

    @PutMapping("/update")
    public Result<Movie> update(@RequestBody Movie movie,String movieReleaseDate,long[] movieType){
        System.out.println(movieReleaseDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate date = LocalDate.parse(movieReleaseDate, formatter);
        LocalDateTime dateTime = date.atStartOfDay();
        movie.setMovieReleaseDate(dateTime);
        movieService.update(movie);

        long[] temp_arr=movieType;
        for(int i=0;i<temp_arr.length;i++){
            movieService.createType(movie.getMovieId(),temp_arr[i]);
        }

        return Result.success(movie);
    }

}
