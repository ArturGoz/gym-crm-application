package com.gca.dto.trainer;

import com.gca.model.TrainingType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TrainerResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private Boolean isActive;
    private TrainingType specialization;
}