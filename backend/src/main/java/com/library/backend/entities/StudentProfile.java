package com.library.backend.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "student_profiles")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    @JoinColumn(name = "student_id", nullable = false)
    Student student;

    String avatar;

    @Column(columnDefinition = "TEXT")
    String preferenceSubjects;

    Double budgetMin;

    Double budgetMax;

    String location;

    @Column(columnDefinition = "TEXT")
    String bio;
}
