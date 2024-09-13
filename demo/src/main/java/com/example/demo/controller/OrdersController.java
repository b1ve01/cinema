package com.example.demo.controller;

import com.example.demo.pojo.Orders;
import com.example.demo.pojo.Result;
import com.example.demo.pojo.Schedule;
import com.example.demo.service.OrdersService;
import com.example.demo.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/create")
    public Result<Orders> create(@RequestBody Orders orders){
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer temp_userId = (Integer) map.get("userId");
        long userId=temp_userId.longValue();
        orders.setUserId(userId);
        LocalDateTime temp_time=LocalDateTime.now();
        orders.setOrdersTime(temp_time);
        ordersService.create(orders);
        return Result.success(orders);
    }

    @GetMapping("/infoByScheduleId")
    public Result<List<Orders>> infoByScheduleId(long scheduleId){
        List<Orders> temp_orders=ordersService.infoByScheduleId(scheduleId);
        return Result.success(temp_orders);
    }

    @GetMapping("/infoSeatFlag")
    public Result<Boolean> infoSeatFlag(String ordersSeat,long scheduleId){
        Boolean seatFlag=false;
        int seat_num=ordersSeat.split(" ").length;
        List<Orders> temp_orders=ordersService.infoByScheduleId(scheduleId);
        for(int i=0;i<seat_num;i++){
            for(int j=0;j<temp_orders.size();j++){
                for(int k=0;k<temp_orders.get(j).getOrdersSeat().split(" ").length;k++){
                    if(ordersSeat.split(" ")[i].equals(temp_orders.get(j).getOrdersSeat().split(" ")[k])){
                        seatFlag=true;
                    }
                }
            }
        }
        return Result.success(seatFlag);
    }

    @GetMapping("/infoByUserId")
    public Result<List<Orders>> infoByUserId(long userId){
        List<Orders> temp_orders=ordersService.infoByUserId(userId);
        return Result.success(temp_orders);
    }

}
