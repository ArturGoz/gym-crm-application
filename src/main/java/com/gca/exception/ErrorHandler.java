package com.gca.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Set;

import static com.gca.exception.ApiError.AUTHENTICATION_ERROR;
import static com.gca.exception.ApiError.DATABASE_ERROR;
import static com.gca.exception.ApiError.INVALID_REQUEST_ERROR;
import static com.gca.exception.ApiError.NOT_FOUND_ERROR;
import static com.gca.exception.ApiError.SERVER_ERROR;
import static com.gca.exception.ApiError.VALIDATION_ERROR;

@ControllerAdvice
@Slf4j
public class ErrorHandler {
    private static final Set<String> BAD_REQUEST_PREFIXES = Set.of(
            "Invalid trainee username"
    );

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorResponse> handleServiceException(ServiceException ex) {
        log.error("ServiceException: {}", ex.getMessage(), ex);
        ApiError error = resolveError(ex);

        return buildErrorResponse(error);
    }

    @ExceptionHandler(DaoException.class)
    public ResponseEntity<ErrorResponse> handleDaoException(DaoException ex) {
        log.error("Database Exception: {}", ex.getMessage(), ex);

        return buildErrorResponse(DATABASE_ERROR);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundExceptions(Exception ex) {
        log.error("Entity not found Exception: {}", ex.getMessage(), ex);

        return buildErrorResponse(NOT_FOUND_ERROR, ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(Exception ex) {
        log.error("Authentication Exception: {}", ex.getMessage(), ex);

        return buildErrorResponse(VALIDATION_ERROR, ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleUnhandledExceptions(Exception ex) {
        log.error("Unhandled Exception: {}", ex.getMessage(), ex);

        return buildErrorResponse(SERVER_ERROR);
    }

    @ExceptionHandler(UserNotAuthenticatedException.class)
    public ResponseEntity<ErrorResponse> handleUserNotAuthenticatedExceptions(Exception ex) {
        log.error("Authentication Exception: {}", ex.getMessage(), ex);

        return buildErrorResponse(AUTHENTICATION_ERROR);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(ApiError apiError) {
        return buildErrorResponse(apiError, null);

    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(ApiError apiError, String message) {
        message = StringUtils.isBlank(message) ? "" : message;

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(String.valueOf(apiError.getCode()))
                .errorMessage(apiError.getMessage() + message)
                .build();

        return new ResponseEntity<>(errorResponse, apiError.getHttpStatus());
    }

    private ApiError resolveError(ServiceException ex) {
        String normalizedMessage = StringUtils.isBlank(ex.getMessage()) ? "" : ex.getMessage().toLowerCase();

        return BAD_REQUEST_PREFIXES.stream()
                .map(String::toLowerCase)
                .filter(normalizedMessage::startsWith)
                .findFirst()
                .map(suffix -> INVALID_REQUEST_ERROR)
                .orElse(SERVER_ERROR);
    }
}
