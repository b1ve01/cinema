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
                .excludePathPatterns("/movie/infoTypeByMovie/**")
                .excludePathPatterns("/movie/infoMovieByState/**");
        ;
    }
}