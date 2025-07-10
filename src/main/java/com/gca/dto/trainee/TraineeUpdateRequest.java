package com.gca.dto.trainee;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TraineeUpdateRequest {
    @NotNull(message = "ID cannot be null")
    private Long id;

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @PastOrPresent(message = "Date of birth must be in the past or present")
    private LocalDate dateOfBirth;

    @Size(max = 255, message = "Address cannot exceed 255 characters")
    private String address;
}
