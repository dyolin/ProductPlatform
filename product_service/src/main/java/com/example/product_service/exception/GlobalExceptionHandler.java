package com.example.product_service.exception;

import com.example.product_service.dto.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<BaseResponse<Object>> handleProductNotFound (ProductNotFoundException exception){
        return build(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Object>> handleValidation(MethodArgumentNotValidException validException){
        List<String> message = new ArrayList<>();
        for(FieldError fieldError : validException.getBindingResult().getFieldErrors()){
            message.add(fieldError.getDefaultMessage());
        }
        return build(HttpStatus.BAD_REQUEST, String.join(",", message));
    }

    private ResponseEntity<BaseResponse<Object>> build (HttpStatus status, String message){
        return ResponseEntity.status(status).body(BaseResponse.error(message));
    }
}
