package com.gca.security;

import com.gca.dao.UserDAO;
import com.gca.dao.transaction.Transactional;
import com.gca.model.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class WebAuthService {
    private static final String COOKIE_NAME = "username";

    private final UserDAO userDAO;

    @Transactional(readOnly = true)
    public Optional<User> getUserFromRequestContext() {
        return getCurrentHttpRequest()
                .flatMap(this::getUsernameFromCookies)
                .map(userDAO::findByUsername);
    }

    private Optional<HttpServletRequest> getCurrentHttpRequest() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return Optional.ofNullable(attributes)
                .map(ServletRequestAttributes::getRequest);
    }

    private Optional<String> getUsernameFromCookies(HttpServletRequest request) {
        if (request.getCookies() == null) return Optional.empty();

        return Arrays.stream(request.getCookies())
                .filter(cookie -> COOKIE_NAME.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }
}
