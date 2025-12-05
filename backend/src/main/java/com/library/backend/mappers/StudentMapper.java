package com.library.backend.mappers;

import com.library.backend.dtos.responses.StudentProfileResponse;
import com.library.backend.dtos.responses.UserResponse;
import com.library.backend.entities.Student;
import com.library.backend.entities.StudentProfile;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface StudentMapper {

    @Mapping(target = "id", source = "profile.student.id")
    @Mapping(target = "user", source = "student")
    StudentProfileResponse toStudentProfileResponse(StudentProfile profile);

    UserResponse toUserResponse(Student student);
}

