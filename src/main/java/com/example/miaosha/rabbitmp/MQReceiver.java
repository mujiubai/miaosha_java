package com.example.miaosha.rabbitmp;

import com.example.miaosha.Service.GoodsService;
import com.example.miaosha.Service.MiaoshaService;
import com.example.miaosha.Service.OrderService;
import com.example.miaosha.domain.MiaoshaOrder;
import com.example.miaosha.domain.MiaoshaUser;
import com.example.miaosha.redis.RedisService;
import com.example.miaosha.result.CodeMsg;
import com.example.miaosha.result.Result;
import com.example.miaosha.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQReceiver {
    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    @Autowired
    RedisService redisService;

    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public void receive(String message) {
        log.info("receive message"+ message);
        MiaoshaMessage mm=RedisService.stringToBean(message,MiaoshaMessage.class);
        MiaoshaUser user=mm.getUser();
        long goodsId=mm.getGoodsId();

        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0) {
            return ;
        }
        //判断是否单用户多次秒杀成功
        MiaoshaOrder order=orderService.getMiaoOrderByUserIdGoodsId(user.getId(), goods.getId());
        if (order != null) {
            return ;
        }
        //减库存，下订单，写入秒杀订单
        miaoshaService.miaosha(user, goods);
    }


//    @RabbitListener(queues = MQConfig.QUEUE)
//    public void receive(String message) {
//        log.info("receive message"+ message);
//    }
//    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
//    public void receiveTopic1(String message) {
//        log.info("receive topic_queue1 message"+ message);
//    }
//    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
//    public void receiveTopic2(String message) {
//        log.info("receive topic_queue2 message"+ message);
//    }
}
