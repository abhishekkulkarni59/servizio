package com.example.servizio.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiErrorCode {
    // Common Success Codes
    SUCCESSFUL_CLIENT_SIGNUP(2100, "Client signed up successfully!"),
    SUCCESSFUL_COMPANY_SIGNUP(2101, "Company signed up successfully!"),
    SUCCESSFUL_LOGIN(2200, "Logged  in successfully!"),
    SUCCESSFUL_AUTHENTICATION(2102, "Authentication successful!"),
    SUCCESSFUL_AD_POST(2103, "Ad posted successfully!"),
    SUCCESSFUL_AD_UPDATE(2104, "Ad updated successfully!"),
    SUCCESSFUL_AD_DELETE(2105, "Ad deleted successfully!"),
    SUCCESSFUL_BOOKING_UPDATE(2106, "Booking status updated successfully!"),
    USER_NOT_FOUND(4000, "User not found"),
    INTERNAL_SERVER_ERROR(5000, "Internal server error"),

    // Common Error Codes
    COMPANY_EXISTS(3101, "Company already exists with this Email!"),
    CLIENT_EXISTS(3102, "Client already exists with this Email!"),
    CLIENT_BOOKING_SUCCESSFUL(3108,"Booking successful."),
    CLIENT_DOES_NOT_EXIST(3109,"User not found."),
    BOOKING_EXIST(3110,"Booking conflict: The ad is already booked for this date."),

    INVALID_CREDENTIALS(3102, "Invalid username or password"),
    AD_POST_FAILED(3103, "Failed to post ad"),
    AD_NOT_FOUND(3104, "Ad not found"),
    AD_UPDATE_FAILED(3105, "Failed to update ad"),
    AD_DELETE_FAILED(3106, "Failed to delete ad"),
    BOOKING_UPDATE_SUCCESS(3107, "Booking status updated successfully");

    private final int code;
    private final String message;

}
