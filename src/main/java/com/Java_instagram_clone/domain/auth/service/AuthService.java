package com.Java_instagram_clone.domain.auth.service;

import com.Java_instagram_clone.domain.auth.entity.Response;
import com.Java_instagram_clone.domain.auth.repository.AuthRepository;
import com.Java_instagram_clone.domain.member.entity.Member;
import com.Java_instagram_clone.domain.member.entity.ResponseMember;
import com.Java_instagram_clone.enums.Authority;
import com.Java_instagram_clone.jwt.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
@Slf4j
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

  @Transactional
  public ResponseEntity<?> login(Member member, HttpServletResponse response) {
    Optional<Member> optionalMember = authRepository.findByEmail(member.getEmail());
    if (optionalMember.isEmpty()) {
      return responseDto.fail("해당하는 유저가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
    }

    Member userInfo = optionalMember.get();

    // 비밀번호 검증 (예: BCryptPasswordEncoder 사용)
    if (!passwordEncoder.matches(member.getPassword(), userInfo.getPassword())) {
      return responseDto.fail("비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED);
    }

    try {
      jwtUtil.loginCreateToken(userInfo.getEmail(), response);

      ResponseMember userResponse = new ResponseMember();
      BeanUtils.copyProperties(userInfo, userResponse, "password");

      return responseDto.success(userResponse, "로그인에 성공했습니다.", HttpStatus.OK);

    } catch (Exception e) {
      log.error("로그인 중 오류 발생: {}", e.getMessage(), e);
      return responseDto.fail("로그인에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  public ResponseEntity<?> logout(HttpServletRequest request) {
    String header = request.getHeader("Authorization");
    if (ObjectUtils.isEmpty(header) || !header.startsWith("Bearer ")) {
      return responseDto.fail("유효한 액세스 토큰이 없습니다.", HttpStatus.BAD_REQUEST);
    }

    String accessToken = header.substring("Bearer ".length());

    try {
      jwtUtil.logout(accessToken);

      return responseDto.success("로그아웃 되었습니다.");

    } catch (ExpiredJwtException e) {
      return responseDto.fail("이미 만료된 토큰입니다.", HttpStatus.BAD_REQUEST);
    } catch (JwtException | IllegalArgumentException e) {
      return responseDto.fail("유효하지 않은 토큰입니다.", HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      log.error("로그아웃 중 오류 발생: {}", e.getMessage(), e);
      return responseDto.fail("로그아웃에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }


}