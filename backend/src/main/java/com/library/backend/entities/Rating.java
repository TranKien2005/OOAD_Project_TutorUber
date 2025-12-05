package com.library.backend.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "ratings")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "tutor_id", nullable = false)
    Tutor tutor;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    Student student;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    Booking booking;

    @Column(nullable = false)
    Integer score;

    @Column(columnDefinition = "TEXT")
    String comment;

    @Column(nullable = false, updatable = false)
    LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
