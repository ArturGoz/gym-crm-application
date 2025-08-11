package com.gca.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.UnsupportedEncodingException;
import java.util.Set;

@Component
public class LoggingInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final Set<String> SENSITIVE_FIELDS = Set.of("password", "oldPassword", "newPassword");
    private static final String EMPTY_BODY = "[empty]";
    private static final String UNSUPPORTED_ENCODING = "[unsupported encoding]";

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        String body = EMPTY_BODY;

        if (request instanceof CachingRequestWrapper cachingRequestWrapper) {
            body = cachingRequestWrapper.getBody();
            body = readBody(body);
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
            body = readBody(body);
        }

        if (ex != null) {
            logger.error("Response for [{} {}] failed with status {}. Error: {}. Body: {}",
                    request.getMethod(), request.getRequestURI(), status, ex.getMessage(), body);
        } else {
            logger.info("Response for [{} {}] returned status {}. Body: {}",
                    request.getMethod(), request.getRequestURI(), status, body);
        }
    }

    private String readBody(String body) {
        if (body == null || body.isEmpty() || EMPTY_BODY.equals(body) || UNSUPPORTED_ENCODING.equals(body)) {
            return body;
        }

        try {
            ObjectNode jsonNode = (ObjectNode) objectMapper.readTree(body);
            hideSensitiveData(jsonNode);
            return jsonNode.toString();
        } catch (Exception e) {
            logger.warn("Failed to parse JSON body: {}", e.getMessage());
            return body;
        }
    }

    private void hideSensitiveData(ObjectNode node) {
        SENSITIVE_FIELDS.stream()
                .filter(node::has)
                .forEach(field -> node.put(field, "***"));
    }

    private String getResponseBody(ContentCachingResponseWrapper response) {
        byte[] buf = response.getContentAsByteArray();

        if (buf.length == 0) {
            return EMPTY_BODY;
        }

        try {
            return new String(buf, response.getCharacterEncoding());
        } catch (UnsupportedEncodingException e) {
            return UNSUPPORTED_ENCODING;
        }
    }
}

