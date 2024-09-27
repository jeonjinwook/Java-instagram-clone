package com.Java_instagram_clone.aop;

import com.Java_instagram_clone.cmn.UserCmnDto;
import com.Java_instagram_clone.domain.auth.repository.AuthRepository;
import com.Java_instagram_clone.domain.member.entity.Member;
import com.Java_instagram_clone.jwt.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class UserCommonDataAspect {

  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String BEARER_PREFIX = "Bearer ";

  private final AuthRepository authRepository;
  private final JwtUtil jwtUtil;

  @Pointcut("execution(public * com.Java_instagram_clone.domain..controller..*(..))")
  private void controllerMethods() {
  }

  @Around("controllerMethods()")
  public Object addUserCommonData(ProceedingJoinPoint joinPoint) throws Throwable {
    setUserCommonData(joinPoint);
    return joinPoint.proceed();
  }

  private void setUserCommonData(ProceedingJoinPoint joinPoint) {

    Object[] args = joinPoint.getArgs();

    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    if (attributes == null) {
      return;
    }

    HttpServletRequest request = attributes.getRequest();

    Member user = getRequestUser(request);

    if (user == null) {
      return;
    }

    for (Object arg : args) {
      if (arg instanceof UserCmnDto) {
        ((UserCmnDto) arg).setUserNo(user.getId());
      }
    }
  }

  private Member getRequestUser(HttpServletRequest request) {

    String token = request.getHeader(AUTHORIZATION_HEADER);

    if (token == null || token.trim().isEmpty() || "null".equalsIgnoreCase(token.trim())) {
      return null;
    }

    if (token.startsWith(BEARER_PREFIX)) {
      token = token.substring(BEARER_PREFIX.length());
    }

    String userName;
    try {
      userName = jwtUtil.getUsernameFromToken(token);
    } catch (ExpiredJwtException e) {
      log.warn("만료된 토큰입니다: {}", e.getMessage());
      return null;
    } catch (Exception e) {
      log.error("토큰에서 사용자명을 추출하는 중 오류 발생: {}", e.getMessage(), e);
      return null;
    }

    return authRepository.findByEmail(userName).orElse(null);
  }
}

