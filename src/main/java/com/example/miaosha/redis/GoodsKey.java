package com.example.miaosha.redis;

import com.example.miaosha.domain.Goods;

public class GoodsKey extends BasePrefix {
    private GoodsKey(int expireSeconds,String prefix) {
        super(expireSeconds,prefix);
    }

    public static GoodsKey getGoodsList = new GoodsKey(60,"gl");
    public static GoodsKey getGoodsDetail = new GoodsKey(60,"gd");
}
