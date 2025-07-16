package com.gca.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Getter
enum ApiError {

    INVALID_REQUEST_ERROR(2400, "Invalid or malformed request data", BAD_REQUEST),
    VALIDATION_ERROR(2760, "Validation error: ", BAD_REQUEST),
    AUTHENTICATION_ERROR(2805, "Authentication fails", UNAUTHORIZED),
    AUTHORIZATION_ERROR(2806, "User is not authorized for request operation", UNAUTHORIZED),
    NOT_FOUND_ERROR(2835, "Requested data was not found: ", NOT_FOUND),
    SERVER_ERROR(3200, "Internal processing error", INTERNAL_SERVER_ERROR),
    DATABASE_ERROR(3358, "Unexpected database access failure", INTERNAL_SERVER_ERROR);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    ApiError(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
