package com.gca.dto.trainer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerCreateRequest {
    private String firstName;
    private String lastName;
    private String specialization;
}
