package com.library.backend.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Student extends User {

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    StudentProfile profile;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    List<Booking> bookings = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    List<Rating> ratings = new ArrayList<>();
}

