package com.library.backend.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "verification_requests")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VerificationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "tutor_id", nullable = false)
    Tutor tutor;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    Status status = Status.PENDING;

    @Column(columnDefinition = "TEXT")
    String note;

    LocalDateTime requestedAt;

    LocalDateTime processedAt;

    @ManyToOne
    @JoinColumn(name = "processed_by")
    Admin processedBy;

    @PrePersist
    protected void onCreate() {
        requestedAt = LocalDateTime.now();
    }

    public enum Status {
        PENDING, APPROVED, REJECTED
    }
}
