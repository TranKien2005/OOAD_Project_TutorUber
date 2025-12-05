package com.library.backend.repositories;

import com.library.backend.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByStudentId(Long studentId);

    List<Booking> findByClassEntityId(Long classId);

    List<Booking> findByStatus(Booking.BookingStatus status);

    Optional<Booking> findByIdAndStudentId(Long id, Long studentId);

    List<Booking> findByClassEntityTutorId(Long tutorId);

}
