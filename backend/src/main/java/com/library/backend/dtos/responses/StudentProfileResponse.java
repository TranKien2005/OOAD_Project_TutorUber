package com.library.backend.dtos.responses;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentProfileResponse {

    Long id;

    String avatar;

    String preferenceSubjects;

    Double budgetMin;

    Double budgetMax;

    String location;

    String bio;

    UserResponse user;
}
