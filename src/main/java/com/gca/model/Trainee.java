package com.gca.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Getter
@SuperBuilder(toBuilder = true)
public class Trainee extends User {
    private final LocalDate dateOfBirth;
    private final String address;

    @Override
    public String toString() {
        return "Trainee{" +
                "userId=" + getUserId() +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", username='" + getUsername() + '\'' +
                ", isActive=" + isActive() +
                '}';
    }
}
