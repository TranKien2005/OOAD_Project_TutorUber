package com.library.backend.mappers;

import com.library.backend.dtos.responses.BookingResponse;
import com.library.backend.entities.Booking;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface BookingMapper {

    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "studentName", source = "student.fullName")
    @Mapping(target = "tutorId", source = "classEntity.tutor.id")
    @Mapping(target = "tutorName", source = "classEntity.tutor.fullName")
    @Mapping(target = "classId", source = "classEntity.id")
    @Mapping(target = "classSubject", source = "classEntity.subject")
    BookingResponse toBookingResponse(Booking booking);

    List<BookingResponse> toBookingResponseList(List<Booking> bookings);
}
