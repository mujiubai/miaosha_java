package com.example.miaosha.redis;

public interface KeyPrefix {
    // 过期时间设置
    public int expireSeconds();

    public String getPrefix();

}
