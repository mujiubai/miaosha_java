package com.example.miaosha.util;

import java.util.UUID;


public class UUIDUtil {
	// 生成cookies的session
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
