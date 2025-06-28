package com.gca.dto.trainee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TraineeResponse {
    private Long userId;
    private String firstName;
    private String lastName;
    private String username;
    private boolean isActive;
    private LocalDate dateOfBirth;
    private String address;
}
