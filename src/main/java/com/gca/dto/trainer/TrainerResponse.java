package com.gca.dto.trainer;

import com.gca.model.TrainingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TrainerResponse {
    private Long id;
    private Long userId;
    private TrainingType specialization;

}