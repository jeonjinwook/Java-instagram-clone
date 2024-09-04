package com.Java_instagram_clone.aop;

import com.Java_instagram_clone.cmn.UserCmnDto;
import com.Java_instagram_clone.domain.auth.repository.AuthRepository;
import com.Java_instagram_clone.domain.member.entity.Member;
import com.Java_instagram_clone.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@org.aspectj.lang.annotation.Aspect
@Component
public class Aspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final AuthRepository authRepository;
    private final JwtUtil jwtUtil;

    public Aspect(AuthRepository authRepository, JwtUtil jwtUtil) {
        this.authRepository = authRepository;
        this.jwtUtil = jwtUtil;
    }

    @Pointcut("execution(public * com.Java_instagram_clone.domain.*.controller.*.*(..))")
    private void getCmnController() {
    }

    @Around("getCmnController()")
    public Object action(ProceedingJoinPoint joinPoint) throws Throwable {
        setUserCmnData(joinPoint);

        Object result = joinPoint.proceed();


        return result;
    }

    private void setUserCmnData(ProceedingJoinPoint joinPoint) {

        Object[] args = joinPoint.getArgs();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        Member user = getRequestUsers(request);

        if (user != null) {

            for (Object arg : args) {

                if (arg instanceof UserCmnDto) {

                    ((UserCmnDto) arg).setUserNo(user.getId());

                }
            }

        }

    }

    private Member getRequestUsers(HttpServletRequest request) {

        String token = request.getHeader("Authorization");

        if (token == null || token.equals("null")) {
            return null;
        }

        String userName = jwtUtil.extractUsername(token.substring(7));

        return authRepository.findByEmail(userName).get();
    }

}