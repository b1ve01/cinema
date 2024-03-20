package com.example.demo.controller;

import com.example.demo.pojo.Result;
import com.example.demo.pojo.User;
import com.example.demo.service.UserService;
import com.example.demo.utils.JwtUtil;
import com.example.demo.utils.MD5Util;
import com.example.demo.utils.ThreadLocalUtil;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private JavaMailSender javaMailSender;

    // 存储验证码及生成时间的 Map
    private Map<String, CodeInfo> Codes = new ConcurrentHashMap<>();

    // 辅助类，用于存储验证码及生成时间
    private static class CodeInfo {
        private final String code;
        private final Instant generationTime;

        public CodeInfo(String code, Instant generationTime) {
            this.code = code;
            this.generationTime = generationTime;
        }

        public String getCode() {
            return code;
        }

        public Instant getGenerationTime() {
            return generationTime;
        }
    }

    // 保存验证码及生成时间
    private void saveCode(String userEmail, String code) {
        Codes.put(userEmail, new CodeInfo(code, Instant.now()));
    }

    //发送邮箱验证码
    @PostMapping("/sendEmail")
    public Result<String> sendEmail(@RequestBody User user) {
        String from = "1203610735@qq.com";
        String subject = "验证 Cinema ID 邮箱地址";
        String code = RandomStringUtils.randomNumeric(6);
        saveCode(user.getUserEmail(), code); // 保存验证码及生成时间
        String text = "您已选用此邮箱地址作为新的 Cinema ID 。要验证此邮箱地址的拥有权，请在邮箱验证页面输入以下验证码：\n\n" + code + "\n\n" + "验证码将会在两分钟后失效。";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(user.getUserEmail());
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
        System.out.println(Codes.get(user.getUserEmail()).getCode());
        System.out.println(Codes.get(user.getUserEmail()).getGenerationTime());
        return Result.success(code);
    }

    //用户注册
    @PostMapping("/register")
    public Result<User> register(@RequestBody User user, String code) {
        //查询用户
        User temp_user = userService.findByEmail(user.getUserEmail());
        if (temp_user == null) {
            CodeInfo codeInfo = Codes.get(user.getUserEmail());
            if (codeInfo != null && codeInfo.getCode().equals(code)) {
                Instant currentTime = Instant.now();
                Instant codeTime = codeInfo.getGenerationTime();
                //验证码生成时的时间和现在时间的时间差
                long time = currentTime.getEpochSecond() - codeTime.getEpochSecond();
                System.out.println(time);
                //验证码有效
                if (time <= 120) {
                    //注册
                    userService.register(user);
                    return Result.success(user);
                } else {
                    return Result.error("验证码已过期");
                }
            } else {
                return Result.error("验证码无效");
            }
        } else {
            return Result.error("用户已存在");
        }
    }

    //用户登录
    @PostMapping("/login")
    public Result<String> login(@RequestBody User user){
        User temp_user=userService.findByEmail(user.getUserEmail());
        if(temp_user==null){
            return Result.error("用户不存在");
        }
        String md5String = MD5Util.MD5Upper(user.getUserPassword());
        if(temp_user.getUserPassword().equals(md5String)){
            Map<String,Object> claims = new HashMap<>();
            claims.put("userId",temp_user.getUserId());
            String token = JwtUtil.genToken(claims);
            //把token存储到redis中
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.set(token,token,60, TimeUnit.HOURS);
            return Result.success(token);
        }
        return Result.error("密码错误");
    }

    //查询用户信息
    @GetMapping("/info")
    public Result<User> info(){
        //根据userId查询用户
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer temp_userId = (Integer) map.get("userId");
        long userId=temp_userId.longValue();
        User user = userService.findById(userId);
        return Result.success(user);
    }

    //更新userName，userPhone和userProfile
    @PutMapping("/update")
    public Result<User> update(@RequestBody User user){
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer temp_userId = (Integer) map.get("userId");
        long userId=temp_userId.longValue();
        User temp_user = userService.findById(userId);
        temp_user.setUserName(user.getUserName());
        temp_user.setUserPhone(user.getUserPhone());
        temp_user.setUserProfile(user.getUserProfile());
        userService.update(temp_user);
        return Result.success(temp_user);
    }

    //更新userUrl
    @PatchMapping("/updateUrl")
    public Result<User> updateUrl(@RequestBody User user){
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer temp_userId = (Integer) map.get("userId");
        long userId=temp_userId.longValue();
        User temp_user = userService.findById(userId);
        temp_user.setUserUrl(user.getUserUrl());
        userService.updateUrl(temp_user);
        return Result.success(temp_user);
    }

    //更新userPassword（记得原密码）
    @PatchMapping("/updatePassword")
    public Result<User> updatePassword(@RequestBody Map<String,String> params,@RequestHeader("Authorization") String token){
        //更新用户密码
        //1.校验参数
        String old_password = params.get("old_password");
        String new_password = params.get("new_password");
        String re_password = params.get("re_password");

        if (!StringUtils.hasLength(old_password) || !StringUtils.hasLength(new_password)|| !StringUtils.hasLength(re_password)){
            return Result.error("缺少必要的参数");
        }

        Map<String, Object> map = ThreadLocalUtil.get();
        Integer temp_userId = (Integer) map.get("userId");
        long userId=temp_userId.longValue();
        User temp_user = userService.findById(userId);

        if (!temp_user.getUserPassword().equals(MD5Util.MD5Upper(old_password))){
            return Result.error("原密码错误");
        }

        if (!new_password.equals(re_password)){
            return Result.error("两次输入的新密码不一致");
        }

        //2.调用service完成密码更新
        userService.updatePassword(new_password);
        //删除redis中对应的token
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.getOperations().delete(token);
        return Result.success(temp_user);
    }

    //更新userPasswordByUserEmail
    @PatchMapping("/updatePasswordByEmail")
    public Result<User> updatePasswordByEmail(@RequestBody User user,String code,@RequestHeader("Authorization") String token){
        //查询用户
        User temp_user = userService.findByEmail(user.getUserEmail());
        String new_password=user.getUserPassword();
        if (temp_user != null) {
            CodeInfo codeInfo = Codes.get(user.getUserEmail());
            if (codeInfo != null && codeInfo.getCode().equals(code)) {
                Instant currentTime = Instant.now();
                Instant codeTime = codeInfo.getGenerationTime();
                //验证码生成时的时间和现在时间的时间差
                long time = currentTime.getEpochSecond() - codeTime.getEpochSecond();
                System.out.println(time);
                //验证码有效
                if (time <= 120) {
                    //更新密码
                    temp_user.setUserPassword(new_password);
                    userService.updatePasswordByEmail(temp_user);
                    //删除redis中对应的token
                    ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
                    operations.getOperations().delete(token);
                    return Result.success(temp_user);
                } else {
                    return Result.error("验证码已过期");
                }
            } else {
                return Result.error("验证码无效");
            }
        } else {
            return Result.error("用户不存在，请先注册");
        }
    }

    //更新userEmail
    @PatchMapping("/updateEmail")
    public Result<User> updateEmail(@RequestBody User user,String code){
        //查询用户
        User temp_user = userService.findByEmail(user.getUserEmail());
        String new_email=user.getUserEmail();
        if (temp_user == null) {
            CodeInfo codeInfo = Codes.get(user.getUserEmail());
            if (codeInfo != null && codeInfo.getCode().equals(code)) {
                Instant currentTime = Instant.now();
                Instant codeTime = codeInfo.getGenerationTime();
                //验证码生成时的时间和现在时间的时间差
                long time = currentTime.getEpochSecond() - codeTime.getEpochSecond();
                System.out.println(time);
                //验证码有效
                if (time <= 120) {
                    //更新邮箱
                    Map<String, Object> map = ThreadLocalUtil.get();
                    Integer temp_userId = (Integer) map.get("userId");
                    long userId=temp_userId.longValue();
                    User temp_user_update_email = userService.findById(userId);
                    temp_user_update_email.setUserEmail(new_email);
                    userService.updateEmail(temp_user_update_email);
                    return Result.success(temp_user_update_email);
                } else {
                    return Result.error("验证码已过期");
                }
            } else {
                return Result.error("验证码无效");
            }
        } else {
            return Result.error("用户已存在");
        }
    }

}