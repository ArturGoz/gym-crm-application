package com.gca.dto.trainer;

import com.gca.dto.trainee.AssignedTraineeDTO;
import com.gca.dto.user.UserGetData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TrainerGetDTO extends UserGetData {
    private String specialization;
    private List<AssignedTraineeDTO> trainees;
}
