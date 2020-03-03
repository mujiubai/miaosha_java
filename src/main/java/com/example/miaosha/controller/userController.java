package com.example.miaosha.controller;

import com.example.miaosha.Service.GoodsService;
import com.example.miaosha.Service.MiaoshaUserService;
import com.example.miaosha.domain.MiaoshaUser;
import com.example.miaosha.redis.MiaoshaUserKey;
import com.example.miaosha.result.Result;
import com.example.miaosha.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/user")
public class userController {
    @Autowired
    private MiaoshaUserService userService;

    @Autowired
    private GoodsService goodsService;


    @RequestMapping("/info")
    @ResponseBody
    public Result<MiaoshaUser> info(Model model, MiaoshaUser user) {
        return Result.success(user);
    }





}
