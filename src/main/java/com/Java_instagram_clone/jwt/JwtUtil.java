package com.Java_instagram_clone.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  private static final long ACCESS_TOKEN_EXPIRE_TIME = 30 * 60 * 1000L;          // 30분
  private static final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L; // 7일
  private static final String BEARER_TYPE = "Bearer ";
  private final Key key;
  private final RedisTemplate<String, String> redisTemplate;

  public JwtUtil(@Value("${jwt.secret}") String secretKey,
      RedisTemplate<String, String> redisTemplate) {
    this.redisTemplate = redisTemplate;
    byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
    this.key = Keys.hmacShaKeyFor(keyBytes);
  }

  public String generateAccessToken(String username) {
    return createToken(username, ACCESS_TOKEN_EXPIRE_TIME);
  }

  public String generateRefreshToken(String username) {
    String refreshToken = createToken(username, REFRESH_TOKEN_EXPIRE_TIME);
    redisTemplate.opsForValue()
        .set("RT:" + username, refreshToken, REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);
    return refreshToken;
  }

  private String createToken(String username, long expireTime) {
    Claims claims = Jwts.claims().setSubject(username);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + expireTime))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  public void validateAccessToken(String token) {
    parseClaims(token);
  }

  public void validateRefreshToken(String token) {
    parseClaims(token);
  }

  public String getUsernameFromToken(String token) {
    return parseClaims(token).getSubject();
  }

  private Claims parseClaims(String token) {
    try {
      return Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(token)
          .getBody();
    } catch (ExpiredJwtException e) {
      throw e;
    } catch (JwtException e) {
      throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
    }
  }

  public String reIssueAccessToken(String username) {
    String refreshToken = redisTemplate.opsForValue().get("RT:" + username);

    if (refreshToken != null) {
      try {
        validateRefreshToken(refreshToken);
        return generateAccessToken(username);
      } catch (ExpiredJwtException e) {
        redisTemplate.delete("RT:" + username);
        return null;
      } catch (IllegalArgumentException e) {
        redisTemplate.delete("RT:" + username);
        return null;
      }
    }

    return null;
  }

  public void loginCreateToken(String username, HttpServletResponse response) {
    String accessToken = generateAccessToken(username);

    response.addHeader("Authorization", BEARER_TYPE + accessToken);
  }

  public long getExpiration(String token) {
    Claims claims = parseClaims(token);
    Date expiration = claims.getExpiration();
    return expiration.getTime() - System.currentTimeMillis();
  }

  public void logout(String accessToken) {
    String username = getUsernameFromToken(accessToken);

    redisTemplate.delete("RT:" + username);

    long expiration = getExpiration(accessToken);
    redisTemplate.opsForValue()
        .set("BL:" + accessToken, "logout", expiration, TimeUnit.MILLISECONDS);
  }

  public boolean isTokenBlacklisted(String token) {
    return redisTemplate.hasKey("BL:" + token);
  }

}

