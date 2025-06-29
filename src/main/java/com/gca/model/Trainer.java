package com.gca.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Getter
@SuperBuilder(toBuilder = true)
public class Trainer extends User {
    private final String specialization;
}
