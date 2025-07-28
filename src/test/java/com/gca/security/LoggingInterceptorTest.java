package com.gca.security;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoggingInterceptorTest {

    @Test
    void shouldMaskOldAndNewPasswordsInLog() {
        LoggingInterceptor interceptor = new LoggingInterceptor();
        Logger logger = (Logger) LoggerFactory.getLogger(LoggingInterceptor.class);
        ListAppender<ILoggingEvent> appender = new ListAppender<>();
        appender.start();
        logger.addAppender(appender);

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
        String message = logEvent.getFormattedMessage();

        assertThat(message)
                .as("Log message masking check")
                .contains("\"oldPassword\":\"***\"")
                .contains("\"newPassword\":\"***\"")
                .doesNotContain("qwerty1234")
                .doesNotContain("qwerty9999");
    }

    @Test
    void shouldMaskPasswordInLoginRequestLog() throws Exception {
        Logger logger = (Logger) LoggerFactory.getLogger(LoggingInterceptor.class);
        ListAppender<ILoggingEvent> appender = new ListAppender<>();
        appender.start();
        logger.addAppender(appender);
        LoggingInterceptor interceptor = new LoggingInterceptor();

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
        String message = logEvent.getFormattedMessage();

        assertThat(message)
                .as("Log message should mask password")
                .contains("\"password\":\"***\"")
                .doesNotContain("superSecret123");
    }
}