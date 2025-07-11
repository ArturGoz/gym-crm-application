package com.gca.service.common;

import com.gca.exception.ServiceException;
import org.springframework.stereotype.Component;

@Component
public class CoreValidator {

    public void validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new ServiceException("Username must not be null or empty");
        }
    }
}
