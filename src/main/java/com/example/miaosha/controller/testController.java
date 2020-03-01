package com.example.miaosha.controller;

import com.example.miaosha.Service.UserService;
import com.example.miaosha.domain.User;
import com.example.miaosha.redis.RedisService;
import com.example.miaosha.redis.UserKey;
import com.example.miaosha.result.CodeMsg;
import com.example.miaosha.result.Result;
import com.example.miaosha.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;

    @RequestMapping("/hello")
    public String home() {
        return "hello!!world，it me";
    }

    @RequestMapping("/user")
    @ResponseBody
    public User getUser() {
        User user = userService.getUser(1);
        return user;
    }

    @RequestMapping("/add")
    @ResponseBody
    public int add() {
        int sta = userService.serUser();
        return sta;
    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> set() {
        User user = new User();
        user.setId(1);
        user.setName("wang");
        redisService.set(UserKey.getById, "" + 2, user);
        return Result.success(true);
    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<User> get() {
        User user = redisService.get(UserKey.getById, "" + 1, User.class);
        return Result.success(user);
    }

}
