package com.toyota.cvqsfinal.exception;


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

    @ExceptionHandler(VehicleNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> vehicleNotFoundException (Exception e, WebRequest request){
        ExceptionResponse exceptionResponse = new ExceptionResponse(LocalDate.now(), e.getMessage(), "VEHICLE");
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(VehicleNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> defectNotFoundException (Exception e, WebRequest request){
        ExceptionResponse exceptionResponse = new ExceptionResponse(LocalDate.now(), e.getMessage(), "DEFECT");
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(VehicleNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> vehicleDefectNotFoundException (Exception e, WebRequest request){
        ExceptionResponse exceptionResponse = new ExceptionResponse(LocalDate.now(), e.getMessage(), "VEHICLEDEFECT");
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(VehicleNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> genericException (Exception e, WebRequest request){
        ExceptionResponse exceptionResponse = new ExceptionResponse(LocalDate.now(), e.getMessage(), "");
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }





}
