package com.example.auth_service.exception;

import com.example.auth_service.dto.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCredentialException.class)
    public ResponseEntity<BaseResponse<Object>> handleInvalidCredential (InvalidCredentialException exception){
        return build(HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<BaseResponse<Object>> handleDuplicateUsername (DuplicateUsernameException exception){
        return build(HttpStatus.BAD_REQUEST, exception.getMessage());
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
