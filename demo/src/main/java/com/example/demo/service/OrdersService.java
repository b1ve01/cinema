package com.example.demo.service;

import com.example.demo.pojo.Orders;
import com.example.demo.pojo.Schedule;

import java.util.List;

public interface OrdersService {

    //查询表中最大的ID
    long getMaxOrdersId();

    void create(Orders orders);

    List<Orders> infoByScheduleId(long scheduleId);

    List<Orders> infoByUserId(long userId);

}
