package com.example.servizio.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private int statusCode;
    private String error;
    private String errorCode; // Optional for custom error codes
    private String message;
    private String path; // The endpoint path where the error occurred
}
