package com.example.miaosha.vo;

import com.example.miaosha.domain.OrderInfo;

public class OrderDetailVo {
    GoodsVo goods;
    OrderInfo order;

    public GoodsVo getGoods() {
        return goods;
    }

    public void setGoods(GoodsVo goods) {
        this.goods = goods;
    }

    public OrderInfo getOrder() {
        return order;
    }

    public void setOrder(OrderInfo order) {
        this.order = order;
    }
}
