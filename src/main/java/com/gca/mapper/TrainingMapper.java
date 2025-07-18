package com.gca.mapper;

import com.gca.dto.training.TrainingCreateRequest;
import com.gca.dto.training.TrainingDTO;
import com.gca.model.Training;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TrainingMapper {
    Training toEntity(TrainingCreateRequest request);

    @Mapping(source = "trainer.id", target = "trainerId")
    @Mapping(source = "trainee.id", target = "traineeId")
    @Mapping(source = "type.id", target = "trainingTypeId")
    TrainingDTO toResponse(Training training);
}

