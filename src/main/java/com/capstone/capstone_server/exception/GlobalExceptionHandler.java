package com.capstone.capstone_server.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
/*
 * 예외 처리기
 */
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  /*
  IllegalArgumentException를 처리하는 함수
  잘못된 파라미터
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex,
      WebRequest request) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  /*
  ServletException을 처리하는 함수
   */
  @ExceptionHandler(ServletException.class)
  public ResponseEntity<Object> handleServletException(ServletException ex, WebRequest request) {
//    String customMsg = "토큰이 틀렸어용!";
    String customMsg = ex.getMessage();
    return new ResponseEntity<>(customMsg, HttpStatus.BAD_REQUEST);
  }

  /*
  IoException을 처리하는 함수
   */
  @ExceptionHandler(IOException.class)
  public ResponseEntity<Object> handleIOException(IOException ex, WebRequest request) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  /*
  MalformedJwtException을 처리하는 함수
   */
  @ExceptionHandler(MalformedJwtException.class)
  public ResponseEntity<Object> handleMalformedJwtException(MalformedJwtException ex,
      WebRequest request) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ExpiredJwtException.class)
  public ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException ex,
      WebRequest request) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(SignatureException.class)
  public ResponseEntity<Object> handleSignatureException(SignatureException ex,
      WebRequest request) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }
}
