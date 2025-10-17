package com.wyt;

import com.wyt.Utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTest {

    @Test
    public void testGenerateJwt() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("id", 1);
        dataMap.put("username", "admin");
        String jwt = Jwts.builder().
                signWith(SignatureAlgorithm.HS256,"MjjllrU=")
                .addClaims(dataMap)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .compact();
        System.out.println(jwt);
    }

        @Test
        public void testParseJWT() {
            String token = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJhZG1pbiIsImV4cCI6MTc2MDYyMzQ1OH0.kxQbGznaf62PRktOSYV4oGTY7D2oEdCGofiRLbd82ww";


            Claims claims = Jwts.parser()
                    .setSigningKey("MjjllrU=")
                    .parseClaimsJws( token)
                    .getBody();

            System.out.println(claims);
        }

}
