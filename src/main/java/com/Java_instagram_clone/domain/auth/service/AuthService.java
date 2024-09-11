package com.Java_instagram_clone.domain.auth.service;

import com.Java_instagram_clone.domain.auth.entity.Response;
import com.Java_instagram_clone.domain.auth.repository.AuthRepository;
import com.Java_instagram_clone.domain.follow.entity.Follow;
import com.Java_instagram_clone.domain.member.entity.Member;
import com.Java_instagram_clone.domain.member.entity.ResponseMember;
import com.Java_instagram_clone.enums.Authority;
import com.Java_instagram_clone.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final Response responseDto;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    public ResponseEntity<?> emailValidation(String email) {

        Member member = authRepository.findByEmail(email).orElse(null);

        if (member != null) {
            return responseDto.fail("이미 가입된 이메일 입니다.", HttpStatus.BAD_REQUEST);
        }

        return responseDto.success("사용할 수 있는 이메일 입니다.");
    }

    public ResponseEntity<?> accountNameValidation(String accountName) {

        Member member = authRepository.findByAccountName(accountName).orElse(null);

        if (member != null) {
            return responseDto.fail("이미 가입한 닉네임 입니다.", HttpStatus.BAD_REQUEST);
        }

        return responseDto.success("사용할 수 있는 닉네임 입니다.");
    }

    public ResponseEntity<?> register(Member user) {
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

        Member userInfo = authRepository.findByEmail(member.getEmail()).orElse(new Member());
        ResponseMember user = new ResponseMember();
        try {

            jwtUtil.loginCreateToken(member, response);

            List<Follow> follower = userInfo.getFollower();
            List<Follow> following = userInfo.getFollowing();

            userInfo.setFollower(follower);
            userInfo.setFollowing(following);

            BeanUtils.copyProperties(userInfo, user);


        } catch (Exception e) {
            return responseDto.fail("로그인에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }


        return responseDto.success(user, "로그인에 성공했습니다.", HttpStatus.CREATED);
    }

    public ResponseEntity<?> logout(HttpServletRequest request) {

        String accessToken = request.getHeader("Authorization").substring(7);

        String userName = jwtUtil.extractUsername(accessToken);

        if (redisTemplate.opsForValue().get("RT:" + userName) != null) {
            redisTemplate.delete("RT:" + userName);
        }

        return responseDto.success("로그아웃 되었습니다.");
    }
}