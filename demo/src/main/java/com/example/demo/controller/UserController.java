package com.example.demo.controller;

import com.example.demo.pojo.Result;
import com.example.demo.pojo.User;
import com.example.demo.service.UserService;
import com.example.demo.utils.JwtUtils;
import com.example.demo.utils.MD5Utils;
import com.example.demo.utils.ThreadLocalUtil;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

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
        User temp_u = userService.findByEmail(user.getUserEmail());
        if (temp_u == null) {
            CodeInfo codeInfo = Codes.get(user.getUserEmail());
            if (codeInfo != null && codeInfo.getCode().equals(code)) {
                Instant currentTime = Instant.now();
                Instant codeTime = codeInfo.getGenerationTime();
                //验证码生成时的时间和现在时间的时间差
                Long time = currentTime.getEpochSecond() - codeTime.getEpochSecond();
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
        User temp_u=userService.findByEmail(user.getUserEmail());
        if(temp_u==null){
            return Result.error("用户不存在");
        }
        String md5String = MD5Utils.MD5Upper(user.getUserPassword());
        if(temp_u.getUserPassword().equals(md5String)){
            Map<String,Object> claims = new HashMap<>();
            claims.put("userId",temp_u.getUserId());
            claims.put("userEmail",temp_u.getUserEmail());
            String token = JwtUtils.genToken(claims);
            return Result.success(token);
        }
        return Result.error("密码错误");
    }

    //查询用户信息
    @GetMapping("/info")
    public Result<User> info(){
        //根据用户email查询用户
        Map<String, Object> map = ThreadLocalUtil.get();
        String userEmail = (String) map.get("userEmail");
        User user = userService.findByEmail(userEmail);
        return Result.success(user);
    }

}