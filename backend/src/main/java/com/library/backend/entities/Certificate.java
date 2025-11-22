package com.library.backend.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "certificates")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "tutor_id", nullable = false)
    Tutor tutor;

    @Column(nullable = false)
    String title;

    String issuedBy;

    LocalDate issuedDate;

    String certificateUrl;

    @Builder.Default
    Boolean verified = false;
}
