package com.gca.mapper;

import com.gca.dto.trainer.TrainerResponse;
import com.gca.dto.trainer.TrainerUpdateResponse;
import com.gca.model.Trainer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {TraineeMapper.class})
public interface TrainerMapper {
    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "specialization.id", target = "specializationId")
    TrainerResponse toResponse(Trainer trainer);

    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.isActive", target = "isActive")
    @Mapping(source = "specialization.id", target = "specializationId")
    @Mapping(source = "trainees", target = "trainees")
    TrainerUpdateResponse toUpdateResponse(Trainer trainer);
}
