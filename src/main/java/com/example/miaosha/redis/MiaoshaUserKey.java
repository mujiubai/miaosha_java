package com.example.miaosha.redis;

public class MiaoshaUserKey extends BasePrefix {
	/**
	 * 有效时间
	 * */
    public static final int TOKEN_EXPIRE = 3600 * 24 * 2;

    private MiaoshaUserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static MiaoshaUserKey token = new MiaoshaUserKey(TOKEN_EXPIRE, "tk");
}
