package com.example.demo.pojo;
//统一响应结果

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Result<T> {

    private Integer code; //业务状态码：0——成功；1——失败
    private String message; //提示信息
    private T data; //响应数据

    //快速返回操作成功的带数据的响应结果
    public static <E> Result<E> success(E data){
        return new Result<>(0,"操作成功",data);
    }

    //快速返回操作成功的响应结果
    public static Result success(){
        return new Result<>(0,"操作成功",null);
    }

    //快速返回操作失败的响应结果
    public static Result error(String message){
        return new Result<>(1,message,null);
    }

}
