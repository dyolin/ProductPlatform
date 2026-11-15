package com.example.scheduler_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BaseResponse<T> {

    private String status;
    private String message;
    private T data;

    public static <T> BaseResponse<T> ok(String message, T data){
        return new BaseResponse<>("success", message, data);
    }

    public static <T> BaseResponse<T> error(String message){
        return new BaseResponse<>("failed", message, null);
    }
}