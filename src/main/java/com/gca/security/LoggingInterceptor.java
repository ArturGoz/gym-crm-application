package com.gca.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.UnsupportedEncodingException;

public class LoggingInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        String body = "[empty]";

        if (request instanceof CachingRequestWrapper) {
            body = ((CachingRequestWrapper) request).getBody();
        }

        logger.info("Incoming Request: [{}] {} Body: {}", request.getMethod(), request.getRequestURI(), body);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {
        int status = response.getStatus();
        String body = "";

        if (response instanceof ContentCachingResponseWrapper wrappedResponse) {
            body = getResponseBody(wrappedResponse);
        }

        if (ex != null) {
            logger.error("Response for [{} {}] failed with status {}. Error: {}. Body: {}",
                    request.getMethod(), request.getRequestURI(), status, ex.getMessage(), body);
        } else {
            logger.info("Response for [{} {}] returned status {}. Body: {}",
                    request.getMethod(), request.getRequestURI(), status, body);
        }
    }

    private String getResponseBody(ContentCachingResponseWrapper response) {
        byte[] buf = response.getContentAsByteArray();

        if (buf.length == 0) {
            return "[empty]";
        }

        try {
            return new String(buf, response.getCharacterEncoding());
        } catch (UnsupportedEncodingException e) {
            return "[unsupported encoding]";
        }
    }
}

