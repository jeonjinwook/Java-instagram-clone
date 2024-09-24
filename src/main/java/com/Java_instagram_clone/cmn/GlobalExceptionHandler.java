package com.Java_instagram_clone.cmn;

import com.Java_instagram_clone.domain.auth.entity.Response;
import io.jsonwebtoken.JwtException;
import io.lettuce.core.RedisException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  private final Response response;

  @ExceptionHandler(JwtException.class)
  public ResponseEntity<?> handleCustomException(JwtException e) {
    log.error("Jwt ERROR{} :", String.valueOf(e));

    return response.fail("Jwt 인증 오류", HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(RedisException.class)
  public ResponseEntity<?> handleRedisException(RedisException e) {
    log.error("Redis ERROR{} :", String.valueOf(e));

    return response.fail("Redis 오류", HttpStatus.BAD_REQUEST);
  }

  //여기부턴 클라이언트 측의 잘못된 요청에 의한 에러를 처리해줌.
  @ExceptionHandler(Exception.class) // nullPointerExceptiono발생시
  protected ResponseEntity<?> handleException(Exception e) {
    log.error("Server ERROR{} :", String.valueOf(e));
    return response.fail("서버 오류", HttpStatus.BAD_REQUEST);
  }


}
