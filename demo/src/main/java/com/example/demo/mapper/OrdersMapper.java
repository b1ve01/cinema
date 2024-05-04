package com.example.demo.mapper;

import com.example.demo.pojo.Orders;
import com.example.demo.pojo.Schedule;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrdersMapper {

    //查询表中最大的ID
    @Select("SELECT IFNULL(MAX(orders_id), 0) FROM orders")
    long getMaxOrdersId();

    @Insert("INSERT INTO orders(orders_id,user_id,schedule_id,orders_seat,orders_state,orders_price,orders_time)" + " VALUES (#{ordersId},#{userId},#{scheduleId},#{ordersSeat},#{ordersState},#{ordersPrice},#{ordersTime})")
    void create(Orders orders);

    @Select("select * from orders where schedule_id = #{scheduleId} and orders_state=1 order by orders_time ASC ")
    List<Orders> infoByScheduleId(long scheduleId);

    @Select("select * from orders where user_id = #{userId} order by orders_time DESC ")
    List<Orders> infoByUserId(long userId);

}
