package com.example.demo.service.impl;

import com.example.demo.mapper.OrdersMapper;
import com.example.demo.pojo.Orders;
import com.example.demo.pojo.Schedule;
import com.example.demo.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private OrdersMapper ordersMapper;

    //查询表中最大的ID
    @Override
    public long getMaxOrdersId() {return ordersMapper.getMaxOrdersId();}

    @Override
    public void create(Orders orders) {
        orders.setOrdersId(getMaxOrdersId() + 1);
        ordersMapper.create(orders);
    }

    @Override
    public List<Orders> infoByScheduleId(long scheduleId){return ordersMapper.infoByScheduleId(scheduleId);}

    @Override
    public List<Orders> infoByUserId(long userId){return ordersMapper.infoByUserId(userId);}

}
