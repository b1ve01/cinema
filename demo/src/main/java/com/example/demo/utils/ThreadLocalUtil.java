package com.example.demo.utils;

public class ThreadLocalUtil {
    //提供TreadLocal对象
    private static final ThreadLocal THREAD_LOCAL = new ThreadLocal();

    //根据键获取值
    public static <T> T get(){
        return (T) THREAD_LOCAL.get();
    }

    //存储键值对
    public static void set(Object value){
        THREAD_LOCAL.set(value);
    }

    //清除ThreadLocal防止内存泄露
    public static void remove(){
        THREAD_LOCAL.remove();
    }
}
