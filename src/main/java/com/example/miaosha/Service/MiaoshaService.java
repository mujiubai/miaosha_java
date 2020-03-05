package com.example.miaosha.Service;

import com.example.miaosha.dao.GoodsDao;
import com.example.miaosha.domain.Goods;
import com.example.miaosha.domain.MiaoshaOrder;
import com.example.miaosha.domain.MiaoshaUser;
import com.example.miaosha.domain.OrderInfo;
import com.example.miaosha.redis.MiaoshaKey;
import com.example.miaosha.redis.RedisService;
import com.example.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MiaoshaService {
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    @Autowired
    RedisService redisService;
    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {
        //减少库存
        boolean success = goodsService.reduceStock(goods);
        if (success) {
            //创建表
            return orderService.creatOrder(user, goods);
        } else {
            setGoodsOver(goods.getId());
            return null;
        }
    }

    /**
     * 获取秒杀结果
     */
    public long getMiaoshaResult(Long userId, long goodsId) {
        MiaoshaOrder order=orderService.getMiaoOrderByUserIdGoodsId(userId, goodsId);
        //成功
        if (order != null) {
            return order.getOrderId();
        }else {
            boolean isOver=getGoodsOver(goodsId);
            if (isOver) {
                return -1;
            }else {
                return 0;
            }
        }
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(MiaoshaKey.isGoodsOver, "" + goodsId, true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(MiaoshaKey.isGoodsOver, "" + goodsId);
    }

}
