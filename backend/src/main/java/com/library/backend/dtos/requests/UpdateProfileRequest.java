package com.library.backend.dtos.requests;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateProfileRequest {

    String fullName;

    String email;

    String phone;

    String avatar;

    String bio;

    // For Student
    String preferenceSubjects;

    Double budgetMin;

    Double budgetMax;

    String location;

    // For Tutor
    Double hourlyRate;

    String specialization;

    String education;

    Integer yearsOfExperience;
}
