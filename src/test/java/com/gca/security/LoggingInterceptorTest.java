package com.gca.security;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.ContentCachingResponseWrapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoggingInterceptorTest {

    private LoggingInterceptor interceptor;
    private ListAppender<ILoggingEvent> appender;

    @BeforeEach
    public void setUp() {
        interceptor = new LoggingInterceptor();
        Logger logger = (Logger) LoggerFactory.getLogger(LoggingInterceptor.class);

        appender = new ListAppender<>();
        appender.start();

        logger.addAppender(appender);
        appender.list.clear();
    }

    @Test
    void shouldMaskOldAndNewPasswordsInLog() {
        String requestBody = """
                {
                    "username": "user1",
                    "oldPassword": "qwerty1234",
                    "newPassword": "qwerty9999"
                }
                """;

        CachingRequestWrapper request = mock(CachingRequestWrapper.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getBody()).thenReturn(requestBody);
        when(request.getMethod()).thenReturn("PUT");
        when(request.getRequestURI()).thenReturn("/api/auth/login");

        interceptor.preHandle(request, response, null);

        ILoggingEvent logEvent = appender.list.iterator().next();

        assertThat(logEvent.getFormattedMessage())
                .as("Log message masking check")
                .contains("\"oldPassword\":\"***\"")
                .contains("\"newPassword\":\"***\"")
                .doesNotContain("qwerty1234")
                .doesNotContain("qwerty9999");
    }

    @Test
    void shouldMaskPasswordInLoginRequestLog() {
        String requestBody = """
                {
                    "username": "user1",
                    "password": "superSecret123"
                }
                """;

        CachingRequestWrapper request = mock(CachingRequestWrapper.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getBody()).thenReturn(requestBody);
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/api/auth/login");

        interceptor.preHandle(request, response, null);

        ILoggingEvent logEvent = appender.list.iterator().next();

        assertThat(logEvent.getFormattedMessage())
                .as("Log message should mask password")
                .contains("\"password\":\"***\"")
                .doesNotContain("superSecret123");
    }

    @Test
    void shouldMaskPasswordInRegisterResponseLog() {
        String responseBody = """
                {
                    "username": "ronnie.coleman",
                    "password": "RawPassword123"
                }
                """;

        ContentCachingResponseWrapper response = mock(ContentCachingResponseWrapper.class);
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(response.getContentAsByteArray()).thenReturn(responseBody.getBytes());
        when(response.getCharacterEncoding()).thenReturn("UTF-8");
        when(response.getContentType()).thenReturn("application/json");
        when(response.getStatus()).thenReturn(200);
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/api/v1/trainees/register");

        interceptor.afterCompletion(request, response, null, null);

        ILoggingEvent logEvent = appender.list.iterator().next();

        assertThat(logEvent.getFormattedMessage())
                .as("Log message should mask password in response")
                .contains("\"password\":\"***\"")
                .doesNotContain("RawPassword123");
    }

    @Test
    void shouldLogHttpMethodAndEndpointAndStatusCodeInResponseLog() {
        String responseBody = """
                {
                    "someField": "someValue"
                }
                """;

        ContentCachingResponseWrapper response = mock(ContentCachingResponseWrapper.class);
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(response.getContentAsByteArray()).thenReturn(responseBody.getBytes());
        when(response.getCharacterEncoding()).thenReturn("UTF-8");
        when(response.getContentType()).thenReturn("application/json");
        when(response.getStatus()).thenReturn(201);

        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/api/v1/trainees/register");

        interceptor.afterCompletion(request, response, null, null);

        ILoggingEvent logEvent = appender.list.iterator().next();

        assertThat(logEvent.getFormattedMessage())
                .as("Log message should contain HTTP method, endpoint, and status code")
                .contains("POST")
                .contains("/api/v1/trainees/register")
                .contains("201");
    }

    @Test
    void shouldLogHttpMethodAndEndpointAndStatusCodeForGetRequest() {
        String responseBody = """
                {
                    "username": "ronnie.coleman"
                }
                """;

        ContentCachingResponseWrapper response = mock(ContentCachingResponseWrapper.class);
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(response.getContentAsByteArray()).thenReturn(responseBody.getBytes());
        when(response.getCharacterEncoding()).thenReturn("UTF-8");
        when(response.getContentType()).thenReturn("application/json");
        when(response.getStatus()).thenReturn(200);

        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/v1/trainees/");

        interceptor.afterCompletion(request, response, null, null);

        ILoggingEvent logEvent = appender.list.iterator().next();

        assertThat(logEvent.getFormattedMessage())
                .as("Log message should contain HTTP method, endpoint, and status code")
                .contains("GET")
                .contains("/api/v1/trainees/")
                .contains("200");
    }

    @Test
    void shouldLogHttpMethodAndEndpointAndStatusCodeForPutRequest() {
        String responseBody = """
                {
                    "result": "success"
                }
                """;

        ContentCachingResponseWrapper response = mock(ContentCachingResponseWrapper.class);
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(response.getContentAsByteArray()).thenReturn(responseBody.getBytes());
        when(response.getCharacterEncoding()).thenReturn("UTF-8");
        when(response.getContentType()).thenReturn("application/json");
        when(response.getStatus()).thenReturn(204);

        when(request.getMethod()).thenReturn("PUT");
        when(request.getRequestURI()).thenReturn("/api/v1/trainees/ronnie.coleman");

        interceptor.afterCompletion(request, response, null, null);

        ILoggingEvent logEvent = appender.list.iterator().next();

        assertThat(logEvent.getFormattedMessage())
                .as("Log message should contain HTTP method, endpoint, and status code")
                .contains("PUT")
                .contains("/api/v1/trainees/ronnie.coleman")
                .contains("204");
    }

    @Test
    void shouldLogHttpMethodAndEndpointInPreHandle() {
        String requestBody = """
                {
                    "username": "testuser",
                    "password": "password123"
                }
                """;

        CachingRequestWrapper request = mock(CachingRequestWrapper.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getBody()).thenReturn(requestBody);
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/api/auth/login");

        interceptor.preHandle(request, response, null);

        ILoggingEvent logEvent = appender.list.iterator().next();

        assertThat(logEvent.getFormattedMessage())
                .as("Log message should contain HTTP method and endpoint")
                .contains("POST")
                .contains("/api/auth/login");
    }

    @Test
    void shouldLogHttpMethodAndEndpointInPreHandleForPut() {
        String requestBody = """
                {
                    "username": "testuser",
                    "oldPassword": "oldpass",
                    "newPassword": "newpass"
                }
                """;

        CachingRequestWrapper request = mock(CachingRequestWrapper.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getBody()).thenReturn(requestBody);
        when(request.getMethod()).thenReturn("PUT");
        when(request.getRequestURI()).thenReturn("/api/auth/change-password");

        interceptor.preHandle(request, response, null);

        ILoggingEvent logEvent = appender.list.iterator().next();

        assertThat(logEvent.getFormattedMessage())
                .as("Log message should contain HTTP method and endpoint")
                .contains("PUT")
                .contains("/api/auth/change-password");
    }
}