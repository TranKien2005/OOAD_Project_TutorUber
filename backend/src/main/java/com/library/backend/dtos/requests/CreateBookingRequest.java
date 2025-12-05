package com.library.backend.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateBookingRequest {

    @NotNull(message = "Class ID is required")
    Long classId;
}
