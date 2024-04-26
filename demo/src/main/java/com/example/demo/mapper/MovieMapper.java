package com.example.demo.mapper;

import com.example.demo.pojo.*;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MovieMapper {

    @Select("SELECT IFNULL(MAX(movie_id), 0) FROM movie")
    long getMaxMovieId();

    @Insert("INSERT INTO moviegenres(movie_id,genre_id)" + " VALUES (#{movieId},#{genreId})")
    void createType(long movieId,long genreId);

    @Insert("INSERT INTO movie(movie_id,movie_name_cn,movie_name_en,movie_director,movie_description,movie_release_date,movie_duration,movie_country,movie_url,movie_state)" + " VALUES (#{movieId},#{movieNameCn},#{movieNameEn},#{movieDirector},#{movieDescription},#{movieReleaseDate},#{movieDuration},#{movieCountry},#{movieUrl},#{movieState})")
    void create(Movie movie);

    @Select("select * from movie ")
    List<Movie> info();

    @Select("select * from movie where movie_id = #{movieId} ")
    Movie infoMovieById(long movieId);

    @Select("select * from movieGenres join genres on movieGenres.genre_id=genres.genre_id where movie_id = #{movieId} ")
    List<MovieGenres> infoTypeByMovie(long movieId);

    @Select("select * from movie where movie_state = #{movieState} order by movie_release_date ASC ")
    List<Movie> infoMovieByState(String movieState);

    @Delete("delete from movie where movie_id = #{movieId} ")
    void deleteByMovieId(long movieId);

    @Delete("delete from moviegenres where movie_id = #{movieId} ")
    void deleteTypeByMovieId(long movieId);

}
