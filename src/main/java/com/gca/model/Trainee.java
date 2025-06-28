package com.gca.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class Trainee extends User {
    private final LocalDate dateOfBirth;
    private final String address;
}
