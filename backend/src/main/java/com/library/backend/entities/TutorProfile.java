package com.library.backend.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "tutor_profiles")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TutorProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    @JoinColumn(name = "tutor_id", nullable = false)
    Tutor tutor;

    String avatar;

    @Column(columnDefinition = "TEXT")
    String bio;

    Double hourlyRate;

    @Builder.Default
    Double rating = 0.0;

    @Builder.Default
    Integer totalRatings = 0;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    VerificationStatus verificationStatus = VerificationStatus.PENDING;

    String specialization;

    String education;

    Integer yearsOfExperience;

    public enum VerificationStatus {
        PENDING, APPROVED, REJECTED
    }
}
