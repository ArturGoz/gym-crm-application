package com.gca.dto.trainer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerUpdateRequest {
    private Long id;
    private Boolean isActive;
    private String specialization;
}
