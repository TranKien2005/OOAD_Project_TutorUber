package com.library.backend.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "classes")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Class {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "tutor_id", nullable = false)
    Tutor tutor;

    @Column(nullable = false)
    String subject;

    @Column(columnDefinition = "TEXT")
    String description;

    @Column(nullable = false)
    Double fee;

    @Column(nullable = false)
    Integer maxStudents;

    @Builder.Default
    Integer currentStudents = 0;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    ClassStatus status = ClassStatus.ACTIVE;

    String location;

    @Builder.Default
    @OneToMany(mappedBy = "classEntity", cascade = CascadeType.ALL)
    List<Schedule> schedules = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "classEntity", cascade = CascadeType.ALL)
    List<Booking> bookings = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    LocalDateTime createdAt;

    LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum ClassStatus {
        ACTIVE, FULL, CLOSED, CANCELLED
    }
}
