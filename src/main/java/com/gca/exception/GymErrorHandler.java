package com.gca.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GymErrorHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Object> handleServiceException(ServiceException ex, WebRequest request) {
        log.error("ServiceException: {}", ex.getMessage(), ex);

        Map<String, Object> body = createResponseBody(
                HttpStatus.BAD_REQUEST.value(),
                "Service Error",
                ex.getMessage(),
                request
        );

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DaoException.class)
    public ResponseEntity<Object> handleDaoException(DaoException ex, WebRequest request) {
        log.error("DaoException: {}", ex.getMessage(), ex);

        Map<String, Object> body = createResponseBody(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Data Access Error",
                "An internal data access error occurred.",
                request
        );
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllOtherExceptions(Exception ex, WebRequest request) {
        log.error("Unhandled Exception: {}", ex.getMessage(), ex);

        Map<String, Object> body = createResponseBody(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Something went wrong.",
                request
        );

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Map<String, Object> createResponseBody(int status, String error,
                                                   String message, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status);
        body.put("error", error);
        body.put("message", message);
        body.put("path", request.getDescription(false).replace("uri=", ""));
        return body;
    }
}
