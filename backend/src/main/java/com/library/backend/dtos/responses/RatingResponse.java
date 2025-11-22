package com.library.backend.dtos.responses;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RatingResponse {

    Long id;

    Long tutorId;

    String tutorName;

    Long studentId;

    String studentName;

    Long bookingId;

    Integer score;

    String comment;

    LocalDateTime createdAt;
}
