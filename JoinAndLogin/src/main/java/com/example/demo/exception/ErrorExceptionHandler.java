package com.example.demo.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ErrorExceptionHandler extends ResponseEntityExceptionHandler{

	@ExceptionHandler(value = {ErrorException.class })
    protected ResponseEntity<ErrorResponse> handleCustomException(ErrorException e) {
        return ErrorResponse.toResponseEntity(e.getErrorCode(), e.getMessage(), e.isHasFieldsError(), e.getFields());
    }
}
