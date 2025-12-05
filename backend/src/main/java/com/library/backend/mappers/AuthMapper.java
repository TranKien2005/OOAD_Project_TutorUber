package com.library.backend.mappers;

import com.library.backend.dtos.requests.RegisterRequest;
import com.library.backend.dtos.responses.UserResponse;
import com.library.backend.entities.*;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface AuthMapper {

    @Mapping(target = "role", expression = "java(mapRole(request.getRole()))")
    User toUser(RegisterRequest request);

    UserResponse toUserResponse(User user);

    default User.UserRole mapRole(String role) {
        return User.UserRole.valueOf(role.toUpperCase());
    }
}
