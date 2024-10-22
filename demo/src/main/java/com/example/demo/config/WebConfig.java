package com.example.demo.config;

import com.example.demo.interceptors.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .excludePathPatterns("/user/register","/user/login","/user/sendEmail","/movie/info")
                .excludePathPatterns("/movie/infoMovieById/**")
                .excludePathPatterns("/upload")
                .excludePathPatterns("/movie/infoTypeByMovie/**")
                .excludePathPatterns("/movie/infoMovieByState/**")
                .excludePathPatterns("/movie/create/**")
                .excludePathPatterns("/cinema/register")
                .excludePathPatterns("/cinema/login")
                .excludePathPatterns("/cinema/infoAllCinema")
                .excludePathPatterns("/cinema/infoById")
                .excludePathPatterns("/schedule/infoById/**")
                .excludePathPatterns("/schedule/infoByMovie/**")
                .excludePathPatterns("/schedule/infoByCinema/**")
                .excludePathPatterns("/schedule/infoByMovieAndCinema/**")
                .excludePathPatterns("/orders/infoByScheduleId/**")
                .excludePathPatterns("/house/infoById/**");
        ;
    }
}