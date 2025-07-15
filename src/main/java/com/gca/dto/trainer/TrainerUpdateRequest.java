package com.gca.dto.trainer;

import com.gca.dto.user.UserUpdateData;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TrainerUpdateRequest extends UserUpdateData {
    @NotBlank(message = "Specialization cannot be blank")
    private String specialization;
}
