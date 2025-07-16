package com.gca.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GymErrorHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Object> handleServiceException(ServiceException ex) {
        log.error("ServiceException: {}", ex.getMessage(), ex);

        Map<String, Object> body = createResponseBody(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DaoException.class)
    public ResponseEntity<Object> handleDaoException(DaoException ex) {
        log.error("DaoException: {}", ex.getMessage(), ex);

        Map<String, Object> body = createResponseBody(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Data Access Error"
        );
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllOtherExceptions(Exception ex) {
        log.error("Unhandled Exception: {}", ex.getMessage(), ex);

        Map<String, Object> body = createResponseBody(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Something went wrong."
        );

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Map<String, Object> createResponseBody(int error, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("errorCode", error);
        body.put("errorMessage", message);
        return body;
    }
}
