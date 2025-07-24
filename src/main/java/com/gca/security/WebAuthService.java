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

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class WebAuthService {
    private final UserDAO userDAO;

    @Transactional(readOnly = true)
    public Optional<User> getUserFromWeb() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null) return Optional.empty();
        HttpServletRequest request = attributes.getRequest();

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
