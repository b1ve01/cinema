package com.example.demo.service;

import com.example.demo.pojo.Genres;
import com.example.demo.pojo.Movie;
import com.example.demo.pojo.MovieGenres;
import com.example.demo.pojo.Result;

import java.util.List;

public interface MovieService {
    List<Movie> info();

    Movie infoMovieById(long movieId);

    List<MovieGenres> infoTypeByMovie(long movieId);

    List<Movie> infoMovieByState(String movieState);

}
