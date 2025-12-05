package com.library.backend.repositories;

import com.library.backend.entities.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    List<Rating> findByTutorId(Long tutorId);

    List<Rating> findByStudentId(Long studentId);

    Optional<Rating> findByBookingId(Long bookingId);

    boolean existsByBookingId(Long bookingId);

}
