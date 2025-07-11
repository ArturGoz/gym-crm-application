package com.gca.security.aspect;

import com.gca.exception.UserNotAuthenticatedException;
import com.gca.model.User;
import com.gca.security.AuthContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationAspect {

    private final AuthContextHolder authContextHolder;

    @Before("@annotation(com.gca.security.Authenticated)")
    public void checkAuthentication() {
        User currentUser = authContextHolder.getCurrentUser();

        Optional.ofNullable(currentUser).orElseThrow(() ->
                new UserNotAuthenticatedException("User not authenticated"));

        log.info("Authenticated user: {}", currentUser.getUsername());
    }
}
