package com.gca.mapper;

import com.gca.dto.trainer.TrainerDTO;
import com.gca.dto.trainer.TrainerUpdateRequest;
import com.gca.dto.trainer.TrainerUpdateDTO;
import com.gca.model.Trainer;
import com.gca.model.TrainingType;
import com.gca.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {TraineeMapper.class})
public interface TrainerMapper {
    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "specialization.name", target = "specialization")
    TrainerDTO toResponse(Trainer trainer);

    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.isActive", target = "isActive")
    @Mapping(source = "specialization.name", target = "specialization")
    @Mapping(source = "trainees", target = "trainees")
    TrainerUpdateDTO toUpdateResponse(Trainer trainer);

    default User fillUserFields(User oldUser, TrainerUpdateRequest request) {
        return oldUser.toBuilder()
                .username(request.getUsername())
                .isActive(request.getIsActive())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();
    }

    default Trainer fillTrainerFields(Trainer oldTrainer, User updatedUser, TrainingType specialization) {
        return oldTrainer.toBuilder()
                .user(updatedUser)
                .specialization(specialization)
                .build();
    }
}
