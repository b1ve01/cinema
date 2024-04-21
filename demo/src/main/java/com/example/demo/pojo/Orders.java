package com.example.demo.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Orders {

    private long ordersId;

    private long userId;

    private long scheduleId;

    private String ordersSeat;

    private String ordersState;

    private String ordersPrice;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime ordersTime;

}
