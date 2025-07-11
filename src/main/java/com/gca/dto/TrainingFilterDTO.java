package com.gca.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TrainingFilterDTO {
    @NotBlank
    private String username;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String relatedName;
    private String trainingTypeName;
}
