package com.gca.model;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
public abstract class User {
    private final Long userId;
    private final String firstName;
    private final String lastName;
    private final String username;
    private final String password;
    private final boolean isActive;

    public abstract User.UserBuilder<?, ?> toBuilder();
}
