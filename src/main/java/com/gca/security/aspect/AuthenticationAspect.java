package com.gca.security.aspect;

import com.gca.exception.UserNotAuthenticatedException;
import com.gca.model.User;
import com.gca.security.WebAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationAspect {

    private final WebAuthService webAuthService;

    @Before("@annotation(com.gca.security.Authenticated)")
    public void checkAuthentication() {
        User currentUser = webAuthService.getUserFromRequestContext()
                .orElseThrow(() -> new UserNotAuthenticatedException("User not authenticated"));

        log.info("Authenticated user: {}", currentUser.getUsername());
    }
}
