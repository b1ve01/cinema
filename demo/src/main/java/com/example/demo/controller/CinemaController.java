package com.example.demo.controller;


import com.example.demo.pojo.Cinema;
import com.example.demo.pojo.Movie;
import com.example.demo.pojo.Result;
import com.example.demo.pojo.User;
import com.example.demo.service.CinemaService;
import com.example.demo.service.UserService;
import com.example.demo.utils.JwtUtil;
import com.example.demo.utils.MD5Util;
import com.example.demo.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/cinema")
public class CinemaController {

    @Autowired
    private CinemaService cinemaService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //用户注册
    @PostMapping("/register")
    public Result<Cinema> register(@RequestBody Cinema cinema){
        //查询用户
        Cinema temp_cinema=cinemaService.findByPhone(cinema.getCinemaPhone());

        if(temp_cinema==null){
            cinemaService.register(cinema);
            return Result.success(cinema);
        }else {
            return Result.error("用户已存在,请修改电话");
        }

    }

    //用户登录
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Cinema cinema){
        Cinema temp_cinema=cinemaService.findByPhone(cinema.getCinemaPhone());
        if(temp_cinema==null){
            return Result.error("用户不存在");
        }
        String md5String = MD5Util.MD5Upper(cinema.getCinemaPassword());
        if(temp_cinema.getCinemaPassword().equals(md5String)){
            Map<String,Object> claims = new HashMap<>();
            claims.put("cinemaId",temp_cinema.getCinemaId());
            String token = JwtUtil.genToken(claims);
            //把token存储到redis中
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.set(token,token,60, TimeUnit.HOURS);

            Map<String, Object> res = new HashMap<>();
            res.put("cinema", temp_cinema);
            res.put("token", token);

            return Result.success(res);
        }
        return Result.error("密码错误");
    }

    //查询用户信息
    @GetMapping("/info")
    public Result<Cinema> info(){
        //根据cinemaId查询用户
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer temp_cinemaId = (Integer) map.get("cinemaId");
        long cinemaId=temp_cinemaId.longValue();
        Cinema cinema = cinemaService.findById(cinemaId);
        return Result.success(cinema);
    }

    //查询全部电影院信息
    @GetMapping("/infoAllCinema")
    public Result<List<Cinema>> infoAllCinema(){
        List<Cinema> res=cinemaService.infoAllCinema();
        return Result.success(res);
    }

    //根据CinemaId查询电影院信息
    @GetMapping("/infoById")
    public Result<Cinema> infoById(long cinemaId){
        Cinema temp_cinema=cinemaService.infoById(cinemaId);
        return Result.success(temp_cinema);
    }

    //删除电影院
    @DeleteMapping("/deleteByCinemaId")
    public Result<Cinema> deleteByCinemaId(long cinemaId){
        cinemaService.deleteByCinemaId(cinemaId);
        return Result.success();
    }

}
