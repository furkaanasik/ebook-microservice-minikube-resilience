package com.ebook.userservice.exception;


import com.ebook.userservice.util.RestUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ProblemDetail handle(RuntimeException e) {
        return RestUtil.createProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, "RuntimeException", e.getMessage());
    }


}
