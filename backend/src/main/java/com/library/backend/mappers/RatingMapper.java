package com.library.backend.mappers;

import com.library.backend.dtos.responses.RatingResponse;
import com.library.backend.entities.Rating;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface RatingMapper {

    @Mapping(target = "tutorId", source = "tutor.id")
    @Mapping(target = "tutorName", source = "tutor.fullName")
    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "studentName", source = "student.fullName")
    @Mapping(target = "bookingId", source = "booking.id")
    RatingResponse toRatingResponse(Rating rating);

    List<RatingResponse> toRatingResponseList(List<Rating> ratings);
}
