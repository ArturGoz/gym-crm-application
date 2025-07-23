package com.gca.security;

import com.gca.dao.UserDAO;
import com.gca.model.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class WebAuthHelper {
    private final UserDAO userDAO;
    private final HttpServletRequest request;

    public Optional<User> getUserFromWeb() {
        Cookie[] cookies = request.getCookies();
        User user = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("username".equals(cookie.getName())) {
                    String username = cookie.getValue();

                    user = userDAO.findByUsername(username);
                }
            }
        }
        return Optional.ofNullable(user);
    }
}
