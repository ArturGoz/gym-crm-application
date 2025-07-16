package com.gca.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

class GymErrorHandlerTest {

    private static final int STATUS_BAD_REQUEST = BAD_REQUEST.value();
    private static final int STATUS_INTERNAL_SERVER_ERROR = INTERNAL_SERVER_ERROR.value();

    private ErrorHandler errorHandler;

    @BeforeEach
    void setUp() {
        errorHandler = new ErrorHandler();
    }

    @Test
    void handleServiceException_shouldReturnBadRequestWithProperBody() {
        String errorMessage = "Something went wrong";
        ServiceException exception = new ServiceException(errorMessage);

        ResponseEntity<Map<String, Object>> response = errorHandler.handleServiceException(exception);

        assertEquals(BAD_REQUEST, response.getStatusCode());

        Map<String, Object> body = response.getBody();

        assertNotNull(body);
        assertEquals(STATUS_BAD_REQUEST, body.get("errorCode"));
        assertEquals(errorMessage, body.get("errorMessage"));
    }

    @Test
    void handleDaoException_shouldReturnInternalServerErrorWithProperBody() {
        String daoErrorMessage = "Database down";
        DaoException exception = new DaoException(daoErrorMessage);

        ResponseEntity<Map<String, Object>> response = errorHandler.handleDaoException(exception);

        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());

        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(STATUS_INTERNAL_SERVER_ERROR, body.get("errorCode"));
        assertEquals("Data Access Error", body.get("errorMessage"));
    }

    @Test
    void handleAllOtherExceptions_shouldReturnInternalServerErrorWithGenericMessage() {
        Exception exception = new Exception("Unexpected failure");

        ResponseEntity<Map<String, Object>> response = errorHandler.handleAllOtherExceptions(exception);

        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());

        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(STATUS_INTERNAL_SERVER_ERROR, body.get("errorCode"));
        assertEquals("Something went wrong.", body.get("errorMessage"));
    }
}