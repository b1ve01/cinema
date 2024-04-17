package com.example.demo.controller;

import com.example.demo.pojo.Cinema;
import com.example.demo.pojo.House;
import com.example.demo.pojo.Result;
import com.example.demo.service.CinemaService;
import com.example.demo.service.HouseService;
import com.example.demo.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/house")
public class HouseController {

    @Autowired
    private HouseService houseService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/create")
    public Result<House> create(@RequestBody House house){
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer temp_cinemaId = (Integer) map.get("cinemaId");
        long cinemaId=temp_cinemaId.longValue();
        house.setCinemaId(cinemaId);
        houseService.create(house);
        return Result.success(house);
    }

    @GetMapping("/infoById")
    public Result<House> infoById(long houseId){
        House temp_house=houseService.infoById(houseId);
        return Result.success(temp_house);
    }

}
