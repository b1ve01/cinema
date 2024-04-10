package com.example.demo.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Movie {

    private long movieId;

    private String movieNameCn;

    private String movieNameEn;

    private String movieDirector;

    private String movieDescription;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime movieReleaseDate;

    private String movieDuration;

    private String movieCountry;

    private String movieUrl;

    private String movieState;

}
