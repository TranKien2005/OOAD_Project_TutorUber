package com.library.backend.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "lessons")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    Booking booking;

    String topic;

    Integer duration;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    CompletionStatus completionStatus = CompletionStatus.SCHEDULED;

    LocalDateTime scheduledAt;

    LocalDateTime completedAt;

    @Column(columnDefinition = "TEXT")
    String notes;

    public enum CompletionStatus {
        SCHEDULED, COMPLETED, CANCELLED, MISSED
    }
}
