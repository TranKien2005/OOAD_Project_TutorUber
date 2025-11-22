package com.library.backend.dtos.responses;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScheduleResponse {

    Long id;

    String dayOfWeek;

    LocalTime startTime;

    LocalTime endTime;

    String location;
}
