package com.example.demo.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Movie {

    private long movieId;

    private String movieNameCn;

    private String movieNameEn;

    private String movieType;

    private String movieDirector;

    private String movieDescription;

    private LocalDateTime movieReleaseDate;

    private String movieDuration;

    private String movieCountry;

    private String movieUrl;

    private String movieState;

}
