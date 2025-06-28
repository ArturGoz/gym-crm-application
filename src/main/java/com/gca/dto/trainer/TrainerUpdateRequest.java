package com.gca.dto.trainer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerUpdateRequest {
    private Long userId;
    private boolean isActive;
    private String specialization;
}
