package com.gca.mapper.rest;

import com.gca.dto.trainee.TraineeCreateDTO;
import com.gca.dto.user.UserCreateDTO;
import com.gca.openapi.model.TraineeCreateRequest;
import com.gca.openapi.model.TraineeCreateResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestTraineeMapper {
    TraineeCreateResponse toRest(UserCreateDTO dto);
    TraineeCreateDTO toDto(TraineeCreateRequest request);
}
