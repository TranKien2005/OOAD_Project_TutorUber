package com.library.backend.dtos.responses;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TutorProfileResponse {

    Long id;

    String avatar;

    String bio;

    Double hourlyRate;

    Double rating;

    Integer totalRatings;

    String verificationStatus;

    String specialization;

    String education;

    Integer yearsOfExperience;

    UserResponse user;

    List<CertificateResponse> certificates;
}
