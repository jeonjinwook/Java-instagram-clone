package com.Java_instagram_clone.jwt;

import com.Java_instagram_clone.domain.auth.service.CustomAuthDetailService;
import com.Java_instagram_clone.domain.member.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 60 * 1000L;              // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;    // 7일
    private final Key key;
    private final CustomAuthDetailService authDetailsService;
    private final RedisTemplate redisTemplate;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final String BEARER_TYPE = "Bearer ";

    public JwtUtil(@Value("${jwt.secret}") String secretKey, CustomAuthDetailService authDetailsService, RedisTemplate redisTemplate, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.authDetailsService = authDetailsService;
        this.redisTemplate = redisTemplate;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails, Long expirTime) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername(), expirTime);
    }

    private String createToken(Map<String, Object> claims, String subject, Long expirTime) {

        return Jwts.builder().setClaims(claims).setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirTime))
                .signWith(key, SignatureAlgorithm.HS256).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public void validate(String token, HttpServletRequest request) throws ExpiredJwtException {

        String username = extractUsername(token);
        UserDetails userDetails = null;
        if (username != null) {

            userDetails = authDetailsService.loadUserByUsername(username);
            if (validateToken(token, userDetails)) {
                setAuthenticationContextHolder(userDetails, request);
            }

        }

    }

    public String reNewAccessTokenFromRefreshToken(String refreshToken,
                                                   HttpServletRequest request) {
            String username = extractUsername(refreshToken);

            UserDetails userDetails = null;

            userDetails = this.authDetailsService.loadUserByUsername(username);

            String token = generateToken(userDetails, ACCESS_TOKEN_EXPIRE_TIME);

            if (validateToken(token, userDetails)) {

                setAuthenticationContextHolder(userDetails, request);

            }
        return BEARER_TYPE + token;
    }

    public String reNewRefreshTokenFromAccessToken(String accessToken,
                                                   HttpServletRequest request) {

        String username = extractUsername(accessToken);

        UserDetails userDetails = null;

        userDetails = this.authDetailsService.loadUserByUsername(username);

        String token = generateToken(userDetails, REFRESH_TOKEN_EXPIRE_TIME);

        if (validateToken(token, userDetails)) {

            setAuthenticationContextHolder(userDetails, request);

            redisTemplate.opsForValue().set("RT:" + userDetails.getUsername(), token, REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);

        }

        return BEARER_TYPE + token;
    }

    private void setAuthenticationContextHolder(UserDetails userDetails,
                                                HttpServletRequest request) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

    public void loginCreateToken(Member member, HttpServletResponse response) {

        String AUTHORIZATION_HEADER = "Authorization";
        String BEARER_TYPE = "Bearer ";
        String REFRESH_AUTHORIZATION_HEADER = "RefreshAuthorization";
        long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;
        long ACCESS_TOKEN_EXPIRE_TIME = 30 * 60 * 1000L;

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                member.getEmail(),
                member.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Object principal = authentication.getPrincipal();

        UserDetails userDetails = (UserDetails) principal;

        String accessToken = generateToken(userDetails, ACCESS_TOKEN_EXPIRE_TIME);

        String refreshToken = generateToken(userDetails, REFRESH_TOKEN_EXPIRE_TIME);

        response.addHeader(AUTHORIZATION_HEADER, BEARER_TYPE + accessToken);

        response.addHeader(REFRESH_AUTHORIZATION_HEADER, BEARER_TYPE + refreshToken);

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set("RT:" + authentication.getName(), refreshToken, REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);


    }
}
