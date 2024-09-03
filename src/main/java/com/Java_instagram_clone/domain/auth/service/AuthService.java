package com.Java_instagram_clone.domain.auth.service;

import com.Java_instagram_clone.domain.auth.entity.Response;
import com.Java_instagram_clone.domain.auth.repository.AuthRepository;
import com.Java_instagram_clone.domain.member.entity.Member;
import com.Java_instagram_clone.enums.Authority;
import com.Java_instagram_clone.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final Response responseDto;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public ResponseEntity<?> emailValidation(String email) {

        Member member = authRepository.findByEmail(email).orElse(null);

        if (member != null) {
            return responseDto.fail("이미 가입된 이메일 입니다.", HttpStatus.BAD_REQUEST);
        }

        return responseDto.success("회원가입에 성공했습니다.");
    }

    public ResponseEntity<?> signUp(Member user) {
        if (authRepository.existsByEmail(user.getEmail())) {
            return responseDto.fail("이미 회원가입된 이메일입니다.", HttpStatus.BAD_REQUEST);
        }

        Member member = Member.builder()
                .email(user.getEmail())
                .name(user.getName())
                .accountName(user.getAccountName())
                .phoneNumber(user.getPhoneNumber())
                .password(passwordEncoder.encode(user.getPassword()))
                .roles(Collections.singletonList(Authority.ROLE_USER.name()))
                .build();
        authRepository.save(member);

        return responseDto.success("회원가입에 성공했습니다.");
    }

    public ResponseEntity<?> login(Member member, HttpServletResponse response) {

        if (authRepository.findByEmail(member.getEmail()).orElse(null) == null) {
            return responseDto.fail("해당하는 유저가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        String AUTHORIZATION_HEADER = "Authorization";
        String BEARER_TYPE = "Bearer ";
        String REFRESH_AUTHORIZATION_HEADER = "RefreshAuthorization";
        long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;
        long ACCESS_TOKEN_EXPIRE_TIME = 30 * 60 * 1000L;

        try {

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    member.getEmail(),
                    member.getPassword());
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            Object principal = authentication.getPrincipal();

            UserDetails userDetails = (UserDetails) principal;

            String accessToken = jwtUtil.generateToken(userDetails, ACCESS_TOKEN_EXPIRE_TIME);

            String refreshToken = jwtUtil.generateToken(userDetails, REFRESH_TOKEN_EXPIRE_TIME);

            response.addHeader(AUTHORIZATION_HEADER, BEARER_TYPE + accessToken);

            response.addHeader(REFRESH_AUTHORIZATION_HEADER, BEARER_TYPE + refreshToken);

            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            valueOperations.set("RT:" + authentication.getName(), refreshToken, REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);

        } catch (Exception e) {
            return responseDto.fail("로그인에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }
        return responseDto.success("로그인에 성공했습니다.");
    }

    public ResponseEntity<?> logout(HttpServletRequest request) {

        String accessToken = request.getHeader("Authorization").substring(7);

        String userName = jwtUtil.extractUsername(accessToken);

        // 3. Redis 에서 해당 User email 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다.
        if (redisTemplate.opsForValue().get("RT:" + userName) != null) {
            // Refresh Token 삭제
            redisTemplate.delete("RT:" + userName);
        }

        return responseDto.success("로그아웃 되었습니다.");
    }

}