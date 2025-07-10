package com.gca.dto.trainer;

import com.gca.model.TrainingType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TrainerCreateRequest {
    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotNull(message = "Specialization cannot be null")
    private TrainingType specialization;
}
