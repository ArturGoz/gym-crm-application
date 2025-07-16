package com.gca.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static com.gca.exception.ApiError.AUTHENTICATION_ERROR;
import static com.gca.exception.ApiError.DATABASE_ERROR;
import static com.gca.exception.ApiError.INVALID_REQUEST_ERROR;
import static com.gca.exception.ApiError.NOT_FOUND_ERROR;
import static com.gca.exception.ApiError.SERVER_ERROR;
import static com.gca.exception.ApiError.VALIDATION_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

class ErrorHandlerTest {

    private ErrorHandler errorHandler;

    @BeforeEach
    void setUp() {
        errorHandler = new ErrorHandler();
    }

    @Test
    void handleServiceException_shouldReturnInvalidRequestError_whenPrefixMatches() {
        String message = "Invalid trainee username format is wrong";
        ServiceException ex = new ServiceException(message);

        ResponseEntity<ErrorResponse> response = errorHandler.handleServiceException(ex);

        assertEquals(BAD_REQUEST, response.getStatusCode());

        ErrorResponse body = response.getBody();

        assertNotNull(body);
        assertEquals(String.valueOf(INVALID_REQUEST_ERROR.getCode()), body.getErrorCode());
        assertTrue(body.getErrorMessage().contains(INVALID_REQUEST_ERROR.getMessage()));
    }

    @Test
    void handleServiceException_shouldReturnServerError_whenPrefixDoesNotMatch() {
        String message = "Some unknown service exception";
        ServiceException ex = new ServiceException(message);

        ResponseEntity<ErrorResponse> response = errorHandler.handleServiceException(ex);

        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());

        ErrorResponse body = response.getBody();

        assertNotNull(body);
        assertEquals(String.valueOf(SERVER_ERROR.getCode()), body.getErrorCode());
        assertEquals(SERVER_ERROR.getMessage(), body.getErrorMessage());
    }

    @Test
    void handleDaoException_shouldReturnDatabaseError() {
        DaoException ex = new DaoException("DB failure");

        ResponseEntity<ErrorResponse> response = errorHandler.handleDaoException(ex);

        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());

        ErrorResponse body = response.getBody();

        assertNotNull(body);
        assertEquals(String.valueOf(DATABASE_ERROR.getCode()), body.getErrorCode());
        assertEquals(DATABASE_ERROR.getMessage(), body.getErrorMessage());
    }

    @Test
    void handleEntityNotFoundExceptions_shouldReturnNotFoundErrorWithMessage() {
        String message = "Trainee with ID 5 not found";
        EntityNotFoundException ex = new EntityNotFoundException(message);

        ResponseEntity<ErrorResponse> response = errorHandler.handleEntityNotFoundExceptions(ex);

        assertEquals(NOT_FOUND, response.getStatusCode());

        ErrorResponse body = response.getBody();

        assertNotNull(body);
        assertEquals(String.valueOf(NOT_FOUND_ERROR.getCode()), body.getErrorCode());
        assertTrue(body.getErrorMessage().contains(NOT_FOUND_ERROR.getMessage()));
        assertTrue(body.getErrorMessage().contains(message));
    }

    @Test
    void handleValidationExceptions_shouldReturnValidationErrorWithMessage() {
        String message = "Field must not be blank";
        ConstraintViolationException ex = new ConstraintViolationException(message, null);

        ResponseEntity<ErrorResponse> response = errorHandler.handleValidationExceptions(ex);

        assertEquals(BAD_REQUEST, response.getStatusCode());

        ErrorResponse body = response.getBody();

        assertNotNull(body);
        assertEquals(String.valueOf(VALIDATION_ERROR.getCode()), body.getErrorCode());
        assertTrue(body.getErrorMessage().contains(VALIDATION_ERROR.getMessage()));
        assertTrue(body.getErrorMessage().contains(message));
    }

    @Test
    void handleUserNotAuthenticatedExceptions_shouldReturnAuthenticationError() {
        UserNotAuthenticatedException ex = new UserNotAuthenticatedException("No token");

        ResponseEntity<ErrorResponse> response = errorHandler.handleUserNotAuthenticatedExceptions(ex);

        assertEquals(UNAUTHORIZED, response.getStatusCode());

        ErrorResponse body = response.getBody();

        assertNotNull(body);
        assertEquals(String.valueOf(AUTHENTICATION_ERROR.getCode()), body.getErrorCode());
        assertEquals(AUTHENTICATION_ERROR.getMessage(), body.getErrorMessage());
    }

    @Test
    void handleUnhandledExceptions_shouldReturnServerError() {
        RuntimeException ex = new RuntimeException("Unknown runtime issue");

        ResponseEntity<ErrorResponse> response = errorHandler.handleUnhandledExceptions(ex);

        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());

        ErrorResponse body = response.getBody();

        assertNotNull(body);
        assertEquals(String.valueOf(SERVER_ERROR.getCode()), body.getErrorCode());
        assertEquals(SERVER_ERROR.getMessage(), body.getErrorMessage());
    }

}