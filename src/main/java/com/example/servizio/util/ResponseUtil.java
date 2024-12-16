package com.example.servizio.util;

import com.example.servizio.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

    public static ResponseEntity<?> buildResponse(int code, String message, Object data) {
        ApiResponse<Object> apiResponse = new com.example.servizio.payload.ApiResponse<>(code, message, data);
        return ResponseEntity.ok(apiResponse);
    }

    public static ResponseEntity<?> buildErrorResponse(int code, String message, HttpStatus status) {
        ApiResponse<Object> apiResponse = new ApiResponse<>(code, message, null);
        return new ResponseEntity<>(apiResponse, status);
    }
    public static ResponseEntity<?> buildSuccessResponse(int code, String message, Object data, HttpStatus status) {
        ApiResponse response = new ApiResponse(code, message, data);
        return new ResponseEntity<>(response, status);
    }


}
