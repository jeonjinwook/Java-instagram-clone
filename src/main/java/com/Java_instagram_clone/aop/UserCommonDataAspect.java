package com.Java_instagram_clone.aop;

import com.Java_instagram_clone.cmn.UserCmnDto;
import com.Java_instagram_clone.domain.auth.repository.AuthRepository;
import com.Java_instagram_clone.domain.member.entity.Member;
import com.Java_instagram_clone.jwt.JwtUtil;
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

  private final AuthRepository authRepository;
  private final JwtUtil jwtUtil;

  @Pointcut("execution(public * com.Java_instagram_clone.domain..controller..*(..))")
  private void getCmnController() {
  }

  @Around("getCmnController()")
  public Object action(ProceedingJoinPoint joinPoint) throws Throwable {
    setUserCmnData(joinPoint);
    return joinPoint.proceed();
  }

  private void setUserCmnData(ProceedingJoinPoint joinPoint) {

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

    String token = request.getHeader("Authorization");

    if (token == null || "null".equalsIgnoreCase(token)) {
      return null;
    }

    if (token.startsWith("Bearer ")) {
      token = token.substring(7);
    }

    String userName;
    try {
      userName = jwtUtil.extractUsername(token);
    } catch (Exception e) {
      log.error("유저 공통 데이터 세팅 중 오류{}", e.getMessage());
      return null;
    }

    return authRepository.findByEmail(userName).orElse(null);
  }
}
