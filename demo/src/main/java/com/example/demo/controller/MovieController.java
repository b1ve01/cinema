package com.example.demo.controller;


import com.example.demo.pojo.Movie;
import com.example.demo.pojo.Result;
import com.example.demo.pojo.User;
import com.example.demo.service.MovieService;
import com.example.demo.service.UserService;
import com.example.demo.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/movie")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/info")
    public Result<List<Movie>> info(){
        List<Movie> res=movieService.info();
        return Result.success(res);
    }


}
