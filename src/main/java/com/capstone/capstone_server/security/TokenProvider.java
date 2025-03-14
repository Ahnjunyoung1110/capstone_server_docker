package com.capstone.capstone_server.security;

import com.capstone.capstone_server.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Date;

@Service
public class TokenProvider {
    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateToken(UserEntity userEntity) {
        Date now = new Date();
        Date endDate = new Date(now.getTime() + 1000 * 60 * 60 * 24);
        return Jwts.builder()
                .subject(userEntity.getUuid())
                .signWith(secretKey)
                .issuer("Capstone Server")
                .issuedAt(now)
                .expiration(endDate)
                .compact();

    }

    public String validateToken(String token) {
        Claims claim = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        //유저 UUId 반환
        return claim.getSubject();
    }
}
