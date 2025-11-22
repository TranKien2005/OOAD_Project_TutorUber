package com.library.backend.dtos.responses;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassResponse {

    Long id;

    Long tutorId;

    String tutorName;

    String subject;

    String description;

    Double fee;

    Integer maxStudents;

    Integer currentStudents;

    String status;

    String location;

    List<ScheduleResponse> schedules;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;
}
