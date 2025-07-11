package com.gca.mapper;

import com.gca.dto.trainer.TrainerCreateRequest;
import com.gca.dto.trainer.TrainerResponse;
import com.gca.dto.trainer.TrainerUpdateRequest;
import com.gca.model.Trainer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TrainerMapper {
    Trainer toEntity(TrainerCreateRequest request);

    Trainer toEntity(TrainerUpdateRequest request);

    @Mapping(source = "user.id", target = "userId")
    TrainerResponse toResponse(Trainer trainer);
}
