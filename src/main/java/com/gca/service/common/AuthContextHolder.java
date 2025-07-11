package com.gca.service.common;

import com.gca.model.User;
import org.springframework.stereotype.Component;

@Component
public class AuthContextHolder {
    private static final ThreadLocal<User> currentUser = new ThreadLocal<>();

    public void setCurrentUser(User user) {
        currentUser.set(user);
    }

    public User getCurrentUser() {
        return currentUser.get();
    }

    public void clear() {
        currentUser.remove();
    }
}
