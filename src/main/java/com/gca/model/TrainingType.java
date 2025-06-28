package com.gca.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class TrainingType {
    private final String name;
}
