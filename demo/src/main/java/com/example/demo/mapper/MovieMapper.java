package com.example.demo.mapper;

import com.example.demo.pojo.Movie;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MovieMapper {

    @Select("select * from movie ")
    List<Movie> info();
}
