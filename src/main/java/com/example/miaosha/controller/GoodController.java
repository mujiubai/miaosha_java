package com.example.miaosha.controller;

import com.example.miaosha.Service.GoodsService;
import com.example.miaosha.Service.MiaoshaUserService;
import com.example.miaosha.domain.MiaoshaUser;
import com.example.miaosha.redis.MiaoshaUserKey;
import com.example.miaosha.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodController {
    @Autowired
    private MiaoshaUserService userService;

    @Autowired
    private GoodsService goodsService;

    /**
     * 此处原先需要对cookies进行处理并最后获得user对象
     * 但将处理逻辑移入了UserArgumentResolver类处理逻辑中
     * 原理是参数传入的时候自动检测到cookies，再对其进行处理，最后返回一个user对象
     */
    @RequestMapping("/to_list")
    public String toList(Model model, MiaoshaUser user) {
        model.addAttribute("user", user);
        List<GoodsVo> goodList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodList);
        return "goods_list";
    }

    @RequestMapping("/to_detail/{goodsId}")
    public String toDetail(Model model, MiaoshaUser user, @PathVariable("goodsId") long goodsId) {
        //snowflake算法可让goodsid不是自增
        model.addAttribute("user", user);
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goods);

        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        //秒杀还没开始，倒计时
        if (now < startAt) {
            miaoshaStatus = 0;
            remainSeconds = (int) (startAt - now) / 1000;
            //秒杀结束
        } else if (now > endAt) {
            miaoshaStatus = 2;
            remainSeconds = -1;
            //秒杀进行中
        } else {
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        return "goods_detail";
    }
}
