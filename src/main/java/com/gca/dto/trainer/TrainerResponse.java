package com.gca.dto.trainer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerResponse {
    private Long userId;
    private String firstName;
    private String lastName;
    private String username;
    private boolean isActive;
    private String specialization;
}