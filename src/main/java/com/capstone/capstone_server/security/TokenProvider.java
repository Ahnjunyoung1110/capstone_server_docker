package com.capstone.capstone_server.security;

import com.capstone.capstone_server.detail.CustomUserDetails;
import com.capstone.capstone_server.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenProvider {

  @Value("${jwt.secret}")
  private String secret;

  private SecretKey secretKey;

  @PostConstruct
  public void init() {
    byte[] keyBytes = Base64.getEncoder().encode(secret.getBytes());
    this.secretKey = Keys.hmacShaKeyFor(keyBytes);
  }

  public String generateToken(UserEntity userEntity) {

    // 🔹 유저의 Role 정보를 String 리스트로 변환 (ex: ["ROLE_ADMIN", "ROLE_USER"])
    String roles = userEntity.getRoles().stream()
        .map(role -> role.getName().name()) // Role Enum이면 `.name()`
        .collect(Collectors.joining(",")); // 여러 개의 Role을 ","로 연결

    Date now = new Date();
    Date endDate = new Date(now.getTime() + 1000 * 60 * 60 * 24); // 24시간 유효
    return Jwts.builder()
        .subject(userEntity.getUuid())
        .signWith(secretKey)
        .claim("roles", roles)
        .issuer("Capstone Server")
        .issuedAt(now)
        .expiration(endDate)
        .compact();

  }

  public CustomUserDetails validateToken(String token) {
    Claims claim = Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();

    // uuid 추출
    String uuid = claim.getSubject();
    // 유저 권한 추출
    List<String> roles = Arrays.asList(claim.get("roles", String.class).split(","));

    return new CustomUserDetails(uuid, roles);
  }
}
