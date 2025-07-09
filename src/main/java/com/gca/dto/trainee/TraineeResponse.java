package com.gca.dto.trainee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TraineeResponse {
    private Long id;
    private Long userId;
    private LocalDate dateOfBirth;
    private String address;
}
