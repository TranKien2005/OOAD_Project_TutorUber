package com.library.backend.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tutors")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Tutor extends User {

    @OneToOne(mappedBy = "tutor", cascade = CascadeType.ALL, orphanRemoval = true)
    TutorProfile profile;

    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL)
    List<Class> classes = new ArrayList<>();

    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL)
    List<Rating> receivedRatings = new ArrayList<>();

    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL)
    List<Certificate> certificates = new ArrayList<>();
}
