package com.gca.mapper;

import com.gca.dto.user.UserCreateRequest;
import com.gca.dto.user.UserDTO;
import com.gca.dto.user.UserUpdateRequest;
import com.gca.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    User toEntity(UserCreateRequest request);

    User toEntity(UserUpdateRequest request);

    UserDTO toResponse(User trainer);
}
