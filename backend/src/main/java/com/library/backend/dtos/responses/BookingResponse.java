package com.library.backend.dtos.responses;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingResponse {

    Long id;

    Long studentId;

    String studentName;

    Long classId;

    String classSubject;

    LocalDateTime bookedDate;

    String status;

    Double totalPrice;

    LocalDateTime confirmedAt;

    LocalDateTime cancelledAt;

    String cancellationReason;

    LocalDateTime createdAt;
}
