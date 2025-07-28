package com.gca.security;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

        ILoggingEvent logEvent = appender.list.get(0);
        String message = logEvent.getFormattedMessage();

        assertFalse(appender.list.isEmpty(), "No log events captured");
        assertTrue(message.contains("\"oldPassword\":\"***\""), "Expected masked oldPassword");
        assertTrue(message.contains("\"newPassword\":\"***\""), "Expected masked newPassword");
        assertFalse(message.contains("qwerty1234"), "Old password should not appear in logs");
        assertFalse(message.contains("qwerty9999"), "New password should not appear in logs");
    }
}