package com.Java_instagram_clone.aop;

import com.Java_instagram_clone.cmn.UserCmnDto;
import com.Java_instagram_clone.domain.auth.repository.AuthRepository;
import com.Java_instagram_clone.domain.member.entity.Member;
import com.Java_instagram_clone.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

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

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      return;
    }

    String username = authentication.getName();
    Member user = authRepository.findByEmail(username).orElse(null);
    if (user == null) {
      return;
    }

    for (Object arg : args) {
      if (arg instanceof UserCmnDto) {
        ((UserCmnDto) arg).setUserNo(user.getId());
      }
    }
  }
}

