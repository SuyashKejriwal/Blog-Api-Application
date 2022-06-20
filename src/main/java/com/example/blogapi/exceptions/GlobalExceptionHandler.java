package com.example.blogapi.exceptions;

import com.example.blogapi.payloads.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.StreamSupport;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException ex){
                String message=ex.getMessage();
                ApiResponse apiResponse=ApiResponse.builder()
                        .message(message)
                        .success(false)
                        .build();

                return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String,String>> handleConstraintViolationException(ConstraintViolationException ex){
        Map<String,String> resp=new HashMap<>();
        ex.getConstraintViolations().forEach(
                (violation)->{
                    String field= StreamSupport.stream(
                                    violation.getPropertyPath().spliterator(), false).
                            reduce((first, second) -> second).
                            orElse(null).
                            toString();
                    String message=violation.getMessage();
                    resp.put(field,message);
                });
        return new ResponseEntity<Map<String,String>>(resp,HttpStatus.BAD_REQUEST);
    }
}
