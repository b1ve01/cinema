package com.example.demo.mapper;

import com.example.demo.pojo.Genres;
import com.example.demo.pojo.Movie;
import com.example.demo.pojo.MovieGenres;
import com.example.demo.pojo.Result;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MovieMapper {

    @Select("select * from movie ")
    List<Movie> info();

    @Select("select * from movie where movie_id = #{movieId} ")
    Movie infoMovieById(long movieId);

    @Select("select * from movieGenres join genres on movieGenres.genre_id=genres.genre_id where movie_id = #{movieId} ")
    List<MovieGenres> infoTypeByMovie(long movieId);

    @Select("select * from movie where movie_state = #{movieState} ")
    List<Movie> infoMovieByState(String movieState);

}
