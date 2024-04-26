package com.example.demo.service.impl;


import com.example.demo.mapper.MovieMapper;
import com.example.demo.pojo.*;
import com.example.demo.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieMapper movieMapper;

    //查询表中最大的ID
    @Override
    public long getMaxMovieId() {return movieMapper.getMaxMovieId();}

    @Override
    public void createType(long movieId,long genreId) {
        movieMapper.createType(movieId,genreId);
    }

    @Override
    public void create(Movie movie) {
        movie.setMovieId(getMaxMovieId() + 1);
        movieMapper.create(movie);
    }

    @Override
    public List<Movie> info(){return movieMapper.info();}

    @Override
    public Movie infoMovieById(long movieId){return movieMapper.infoMovieById(movieId);}

    @Override
    public List<MovieGenres> infoTypeByMovie(long movieId){return movieMapper.infoTypeByMovie(movieId);}

    @Override
    public List<Movie> infoMovieByState(String movieState){return movieMapper.infoMovieByState(movieState);}

    @Override
    public void deleteByMovieId(long movieId){movieMapper.deleteByMovieId(movieId);movieMapper.deleteTypeByMovieId(movieId);}

}
