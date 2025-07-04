package com.gca.dto.trainee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TraineeResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private Boolean isActive;
    private LocalDate dateOfBirth;
    private String address;
}
