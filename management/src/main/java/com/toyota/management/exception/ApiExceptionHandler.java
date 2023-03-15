package com.toyota.management.exception;


import feign.FeignException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDate;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<ExceptionResponse> illegalException (Exception e, WebRequest request){
        ExceptionResponse exceptionResponse = new ExceptionResponse(LocalDate.now(), e.getMessage(), "ARGUMENT");
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> userNotFoundException (Exception e, WebRequest request){
        ExceptionResponse exceptionResponse = new ExceptionResponse(LocalDate.now(), e.getMessage(), "USER");
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GenericException.class)
    public final ResponseEntity<ExceptionResponse> genericException (Exception e, WebRequest request){
        ExceptionResponse exceptionResponse = new ExceptionResponse(LocalDate.now(), e.getMessage(), "");
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }







}