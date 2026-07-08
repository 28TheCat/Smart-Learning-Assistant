package com.wyt.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    private final String signKey;
    private final Long expire;
    private final List<String> previousSignKeys;

    public JwtUtils(
            @Value("${app.jwt.sign-key}") String signKey,
            @Value("${app.jwt.expire-millis}") Long expire,
            @Value("${app.jwt.previous-sign-keys:}") String previousSignKeys) {
        if (!StringUtils.hasText(signKey)) {
            throw new IllegalArgumentException("JWT sign key must not be blank");
        }
        this.signKey = signKey;
        this.expire = expire;
        this.previousSignKeys = Arrays.stream(previousSignKeys.split(","))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .collect(Collectors.toList());
    }

    /**
     * 生成JWT令牌
     * @return
     */
    public String generateJwt(Map<String,Object> claims){
        String jwt = Jwts.builder()
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, signKey)
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .compact();
        return jwt;
//        return Jwts.builder()
//                .addClaims(claims)
//                .signWith(SignatureAlgorithm.HS256, signKey)
//                .compact();
    }

    /**
     * 解析JWT令牌
     * @param jwt JWT令牌
     * @return JWT第二部分负载 payload 中存储的内容
     */
    public Claims parseJWT(String jwt){
        try {
            return parseJWT(jwt, signKey);
        } catch (JwtException e) {
            for (String previousSignKey : previousSignKeys) {
                try {
                    return parseJWT(jwt, previousSignKey);
                } catch (JwtException ignored) {
                    // Try the next rotation key.
                }
            }
            throw e;
        }
    }

    private Claims parseJWT(String jwt, String signingKey) {
        return Jwts.parser()
                .setSigningKey(signingKey)
                .parseClaimsJws(jwt)
                .getBody();
    }
}
