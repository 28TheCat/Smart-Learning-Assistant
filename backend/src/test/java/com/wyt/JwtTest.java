package com.wyt;

import com.wyt.Utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTest {

    @Test
    void testGenerateJwt() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("id", 1);
        dataMap.put("username", "admin");

        String jwt = JwtUtils.generateJwt(dataMap);

        assertThat(jwt).isNotBlank();
    }

    @Test
    void testParseJWT() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("id", 1);
        dataMap.put("username", "admin");
        String token = JwtUtils.generateJwt(dataMap);

        Claims claims = JwtUtils.parseJWT(token);

        assertThat(claims.get("id")).isEqualTo(1);
        assertThat(claims.get("username")).isEqualTo("admin");
        assertThat(claims.getExpiration()).isNotNull();
    }

}
