package com.gca.mapper;

import com.gca.dto.trainee.TraineeCreateRequest;
import com.gca.dto.trainee.TraineeDTO;
import com.gca.dto.trainee.TraineeUpdateData;
import com.gca.dto.trainee.TraineeUpdateDTO;
import com.gca.model.Trainee;
import com.gca.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import static java.util.Optional.ofNullable;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {TrainerMapper.class}
)
public interface TraineeMapper {
    Trainee toEntity(TraineeCreateRequest request);

    Trainee toEntity(TraineeUpdateData request);

    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.username", target = "username")
    TraineeDTO toResponse(Trainee trainee);

    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.isActive", target = "isActive")
    @Mapping(source = "trainers", target = "trainers")
    TraineeUpdateDTO toUpdateResponse(Trainee trainee);

    default User fillUserFields(User oldUser, TraineeUpdateData request) {
        return oldUser.toBuilder()
                .username(request.getUsername())
                .isActive(request.getIsActive())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();
    }

    default Trainee fillTraineeFields(Trainee oldTrainee, User updatedUser, TraineeUpdateData request) {
        return oldTrainee.toBuilder()
                .user(updatedUser)
                .dateOfBirth(ofNullable(request.getDateOfBirth())
                                .orElse(oldTrainee.getDateOfBirth()))
                .address(ofNullable(request.getAddress())
                                .orElse(oldTrainee.getAddress()))
                .build();
    }
}
