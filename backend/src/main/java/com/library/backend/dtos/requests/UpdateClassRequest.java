package com.library.backend.dtos.requests;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateClassRequest {

    String subject;

    String description;

    Double fee;

    Integer maxStudents;

    String location;

    String status;

    List<CreateClassRequest.ScheduleRequest> schedules;
}
