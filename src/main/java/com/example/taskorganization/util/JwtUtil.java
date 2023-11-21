package com.example.taskorganization.util;

import com.example.taskorganization.dao.entity.UserEntity;
import com.example.taskorganization.model.response.SignInResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${spring.security.key}")
    private String secretkey;

    @Value("${spring.security.accessToken.time}")
    private Long accessExpirationTime;

    @Value("${spring.security.refreshToken.time}")
    private Long refreshExpirationTime;
    private Key getSingingKey(){
        byte[] keyBytes;
        keyBytes= Decoders.BASE64.decode(secretkey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSingingKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public SignInResponse generateTokens(UserEntity user){

        String accessToken=Jwts.builder()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*accessExpirationTime))
                .signWith(getSingingKey(), SignatureAlgorithm.HS256)
                .claim("userId",user.getId())
                .claim("username",user.getUsername())
                .claim("email",user.getEmail())
                .compact();

        String refreshToken=Jwts.builder()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*refreshExpirationTime))
                .signWith(getSingingKey(), SignatureAlgorithm.HS256)
                .claim("userId",user.getId())
                .claim("username",user.getUsername())
                .claim("email",user.getEmail())
                .compact();

        return SignInResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

    }


}
