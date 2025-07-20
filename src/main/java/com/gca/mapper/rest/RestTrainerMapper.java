package com.gca.mapper.rest;

import com.gca.dto.trainer.AssignedTrainerDTO;
import com.gca.openapi.model.AssignedTrainerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestTrainerMapper {
    AssignedTrainerResponse toRest(AssignedTrainerDTO dto);
}
