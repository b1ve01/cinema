package com.example.demo.controller;


import com.example.demo.pojo.*;
import com.example.demo.service.MovieService;
import com.example.demo.service.UserService;
import com.example.demo.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
