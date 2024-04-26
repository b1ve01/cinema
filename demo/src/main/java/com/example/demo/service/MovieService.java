package com.example.demo.service;

import com.example.demo.pojo.*;

import java.util.List;

public interface MovieService {

    long getMaxMovieId();

    void create(Movie movie);

    void createType(long movieId,long genreId);

    List<Movie> info();

    Movie infoMovieById(long movieId);

    List<MovieGenres> infoTypeByMovie(long movieId);

    List<Movie> infoMovieByState(String movieState);

    void deleteByMovieId(long movieId);

}
