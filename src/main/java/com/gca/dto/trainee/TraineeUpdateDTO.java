package com.gca.dto.trainee;

import com.gca.dto.trainer.TrainerDTO;
import com.gca.dto.user.UserUpdateData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TraineeUpdateDTO extends UserUpdateData {
    private LocalDate dateOfBirth;
    private String address;
    private List<TrainerDTO> trainers;
}
