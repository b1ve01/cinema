package com.example.demo.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtil {
    private static final String KEY = "cinema";

    //接受业务数据，生成token并返回
    public static String genToken(Map<String, Object> claims) {
        String token = JWT.create()
                .withClaim("claims",claims)//添加载荷
                .withExpiresAt(new Date(System.currentTimeMillis()+1000*60*60*60))//添加过期时间
                .sign(Algorithm.HMAC256(KEY));
        return token;
    }

    //接收token，验证token,返回业务数据
    public static Map<String, Object> parseToken(String token) {
        return JWT.require(Algorithm.HMAC256(KEY))
                .build()
                .verify(token)
                .getClaim("claims")
                .asMap();
    }
}
