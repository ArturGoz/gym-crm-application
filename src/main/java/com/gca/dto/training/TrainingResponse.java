package com.gca.dto.training;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TrainingResponse {
    private Long id;
    private Long trainerId;
    private Long traineeId;
    private Long trainingTypeId;
    private LocalDate date;
    private Long duration;
    private String name;
}


