package com.wyt;

import java.util.Map;
import static com.wyt.Utils.JwtUtils.generateJwt;

public class Main {
    public static void main(String[] args){
        Map<String, Object> claims = Map.of("id", 2); // 测试用户ID
        String token =generateJwt(claims);
        System.out.println("测试JWT: " + token);
    }

}
