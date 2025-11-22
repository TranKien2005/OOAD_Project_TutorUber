package com.library.backend.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateClassRequest {

    @NotBlank(message = "Subject is required")
    String subject;

    String description;

    @NotNull(message = "Fee is required")
    Double fee;

    @NotNull(message = "Max students is required")
    Integer maxStudents;

    String location;

    List<ScheduleRequest> schedules;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ScheduleRequest {
        String dayOfWeek;
        String startTime;
        String endTime;
        String location;
    }
}
