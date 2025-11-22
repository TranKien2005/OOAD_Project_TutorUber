package com.library.backend.mappers;

import com.library.backend.dtos.responses.ClassResponse;
import com.library.backend.dtos.responses.ScheduleResponse;
import com.library.backend.entities.Class;
import com.library.backend.entities.Schedule;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ClassMapper {

    @Mapping(target = "tutorId", source = "tutor.id")
    @Mapping(target = "tutorName", source = "tutor.fullName")
    ClassResponse toClassResponse(Class classEntity);

    List<ClassResponse> toClassResponseList(List<Class> classes);

    ScheduleResponse toScheduleResponse(Schedule schedule);

    List<ScheduleResponse> toScheduleResponseList(List<Schedule> schedules);
}
