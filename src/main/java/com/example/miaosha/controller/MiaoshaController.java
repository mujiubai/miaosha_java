package com.example.miaosha.controller;

import com.example.miaosha.Service.GoodsService;
import com.example.miaosha.Service.MiaoshaService;
import com.example.miaosha.Service.OrderService;
import com.example.miaosha.domain.MiaoshaOrder;
import com.example.miaosha.domain.MiaoshaUser;
import com.example.miaosha.domain.OrderInfo;
import com.example.miaosha.rabbitmp.MQSender;
import com.example.miaosha.rabbitmp.MiaoshaMessage;
import com.example.miaosha.redis.GoodsKey;
import com.example.miaosha.redis.RedisService;
import com.example.miaosha.result.CodeMsg;
import com.example.miaosha.result.Result;
import com.example.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {
    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    @Autowired
    RedisService redisService;

    @Autowired
    MQSender sender;

    private Map<Long,Boolean> localOverMap= new HashMap<Long, Boolean>();

    /**
     * 系统初始化 就调用此方法
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        if (goodsList == null) {
            return;
        }
        for (GoodsVo goods : goodsList) {
            redisService.set(GoodsKey.getMiaoshaGoodsStock, "" + goods.getId(), goods.getStockCount());
            localOverMap.put(goods.getId(), false);
        }
    }


    /**
     * 无优化：                       1000*20  第一次qps2200 第二次2500
     * redis:                         1000*20 3500    1000*100 4400
     * redis+rabbitmq+本地内存标识:   1000*20 6500    1000*100 8500
     * GET POST 区别
     * get幂等，
     * post
     */
    @RequestMapping(value = "/do_miaosha", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> miaosha(Model model, MiaoshaUser user, @RequestParam("goodsId") long goodsId) {
        if (user == null) {
            return Result.error(CodeMsg.SERVER_ERROR);
        }
        model.addAttribute("user", user);

        //内存标记，减少redis访问量
        boolean over = localOverMap.get(goodsId);
        if (over) {
            return Result.error(CodeMsg.MIAOSHA_OVER);
        }
        long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, "" + goodsId);
        //判断是否还有库存
        if (stock < 0) {
            localOverMap.put(goodsId, true);
            return Result.error(CodeMsg.MIAOSHA_OVER);
        }
        //判断是否单用户多次秒杀成功
        MiaoshaOrder order = orderService.getMiaoOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return Result.error(CodeMsg.MIAOSHA_REPEATE);
        }
        //入队
        MiaoshaMessage mm = new MiaoshaMessage();
        mm.setGoodsId(goodsId);
        mm.setUser(user);
        sender.sendMiaoshaMessage(mm);
        return Result.success(0);// 排队中
        /*
        //判断是否还有库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0) {
            return Result.error(CodeMsg.MIAOSHA_OVER);
        }
        //判断是否单用户多次秒杀成功
        MiaoshaOrder order=orderService.getMiaoOrderByUserIdGoodsId(user.getId(), goods.getId());
        if (order != null) {
            return Result.error(CodeMsg.MIAOSHA_REPEATE);
        }
        //减库存，下订单，写入秒杀订单
        OrderInfo orderInfo=miaoshaService.miaosha(user, goods);
        return Result.success(orderInfo);*/
    }
    /**
     * orderOd:成功
     * -1：失败
     * 0：还在排队中
     * */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> miaoshaResult(Model model, MiaoshaUser user, @RequestParam("goodsId") long goodsId) {
        if (user == null) {
            return Result.error(CodeMsg.SERVER_ERROR);
        }
        model.addAttribute("user", user);
        long result=miaoshaService.getMiaoshaResult(user.getId(), goodsId);
        return Result.success(result);
    }
}
