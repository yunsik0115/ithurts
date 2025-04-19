package com.sidepj.ithurts.controller.interceptor.ExceptionHandler;

import com.sidepj.ithurts.controller.dto.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@Slf4j
@RestControllerAdvice
public class InterceptorExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResult> loginExceptionHandler(AccessDeniedException e){
        log.error("[Login Error] = {}", e.getMessage());
        ErrorResult errorResult = new ErrorResult(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }
}
