package com.gca.dto.trainee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TraineeUpdateRequest {
    private Long userId;
    private boolean isActive;
    private LocalDate dateOfBirth;
    private String address;
}
