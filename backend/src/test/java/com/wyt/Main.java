package com.wyt;

import com.wyt.Utils.JwtUtils;

import java.util.Map;

public class Main {
    public static void main(String[] args){
        Map<String, Object> claims = Map.of("id", 2); // 测试用户ID
        JwtUtils jwtUtils = new JwtUtils("test-sign-key-for-local-main", 43200000L, "");
        String token = jwtUtils.generateJwt(claims);
        System.out.println("测试JWT: " + token);
    }

}
