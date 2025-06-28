package com.gca.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class User {
    private final Long userId;
    private final String firstName;
    private final String lastName;
    private final String username;
    private final String password;
    private final boolean isActive;
}
