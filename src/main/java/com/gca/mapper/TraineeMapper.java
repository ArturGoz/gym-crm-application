package com.gca.mapper;

import com.gca.dto.trainee.TraineeCreateRequest;
import com.gca.dto.trainee.TraineeResponse;
import com.gca.dto.trainee.TraineeUpdateRequest;
import com.gca.model.Trainee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TraineeMapper {
    Trainee toEntity(TraineeCreateRequest request);

    Trainee toEntity(TraineeUpdateRequest request);

    @Mapping(source = "user.id", target = "userId")
    TraineeResponse toResponse(Trainee trainee);
}
