package com.library.backend.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true, nullable = false)
    String username;

    @Column(nullable = false)
    String password;

    @Column(nullable = false)
    String fullName;

    @Column(unique = true, nullable = false)
    String email;

    String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    UserRole role;

    @Builder.Default
    @Column(nullable = false)
    Boolean active = true;

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

    public enum UserRole {
        STUDENT, TUTOR, ADMIN
    }
}
