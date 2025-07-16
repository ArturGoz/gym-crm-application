package com.gca.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
@Slf4j
public class ErrorHandler {

    private static final int STATUS_BAD_REQUEST = BAD_REQUEST.value();
    private static final int STATUS_INTERNAL_SERVER_ERROR = INTERNAL_SERVER_ERROR.value();

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Map<String, Object>> handleServiceException(ServiceException ex) {
        log.error("ServiceException: {}", ex.getMessage(), ex);

        Map<String, Object> body = createResponseBody(
                STATUS_BAD_REQUEST,
                ex.getMessage()
        );

        return new ResponseEntity<>(body, BAD_REQUEST);
    }

    @ExceptionHandler(DaoException.class)
    public ResponseEntity<Map<String, Object>> handleDaoException(DaoException ex) {
        log.error("DaoException: {}", ex.getMessage(), ex);

        Map<String, Object> body = createResponseBody(
                STATUS_INTERNAL_SERVER_ERROR,
                "Data Access Error"
        );
        return new ResponseEntity<>(body, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAllOtherExceptions(Exception ex) {
        log.error("Unhandled Exception: {}", ex.getMessage(), ex);

        Map<String, Object> body = createResponseBody(
                STATUS_INTERNAL_SERVER_ERROR,
                "Something went wrong."
        );

        return new ResponseEntity<>(body, INTERNAL_SERVER_ERROR);
    }

    private Map<String, Object> createResponseBody(int error, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("errorCode", error);
        body.put("errorMessage", message);

        return body;
    }
}
