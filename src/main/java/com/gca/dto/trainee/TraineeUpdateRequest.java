package com.gca.dto.trainee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder(toBuilder = true)@NoArgsConstructor
@AllArgsConstructor
public class TraineeUpdateRequest {
    private Long userId;
    private boolean isActive;
    private LocalDate dateOfBirth;
    private String address;
}
