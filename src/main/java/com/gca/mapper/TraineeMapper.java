package com.gca.mapper;

import com.gca.dto.trainee.TraineeCreateRequest;
import com.gca.dto.trainee.TraineeResponse;
import com.gca.dto.trainee.TraineeUpdateRequest;
import com.gca.model.Trainee;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TraineeMapper {
    Trainee toEntity(TraineeCreateRequest request);
    Trainee toEntity(TraineeUpdateRequest request);

    @Mapping(source = "active", target = "isActive")
    TraineeResponse toResponse(Trainee trainee);
}
