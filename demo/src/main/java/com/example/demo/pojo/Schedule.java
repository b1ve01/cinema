package com.example.demo.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Schedule {

    private long scheduleId;

    private long movieId;

    private long cinemaId;

    private long houseId;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
    private LocalDateTime scheduleTime;

    private String schedulePrice;

    private String scheduleRemain;

    private String scheduleState;

    private String scheduleDescription;

}
