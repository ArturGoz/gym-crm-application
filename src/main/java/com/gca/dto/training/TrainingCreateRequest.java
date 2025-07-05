package com.gca.dto.training;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TrainingCreateRequest {
    private Long trainerId;
    private Long traineeId;
    private Long trainingTypeId;
    private LocalDate date;
    private Long duration;
    private String name;
}
