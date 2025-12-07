package com.library.backend.services;

import com.library.backend.dtos.requests.CreateBookingRequest;
import com.library.backend.dtos.responses.BookingResponse;
import com.library.backend.entities.Booking;
import com.library.backend.entities.Class;
import com.library.backend.entities.Student;
import com.library.backend.exceptions.GeneralException;
import com.library.backend.exceptions.ResponseCode;
import com.library.backend.mappers.BookingMapper;
import com.library.backend.repositories.BookingRepository;
import com.library.backend.repositories.ClassRepository;
import com.library.backend.repositories.StudentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BookingControl {

    BookingRepository bookingRepository;
    StudentRepository studentRepository;
    ClassRepository classRepository;
    BookingMapper bookingMapper;

    @Transactional
    public BookingResponse createBooking(Long studentId, CreateBookingRequest request) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new GeneralException(ResponseCode.STUDENT_NOT_FOUND));

        Class classEntity = classRepository.findById(request.getClassId())
                .orElseThrow(() -> new GeneralException(ResponseCode.CLASS_NOT_FOUND));

        // Check if class is active
        if (classEntity.getStatus() != Class.ClassStatus.ACTIVE) {
            throw new GeneralException(ResponseCode.CLASS_NOT_ACTIVE);
        }

        // Check if class is full
        if (classEntity.getCurrentStudents() >= classEntity.getMaxStudents()) {
            throw new GeneralException(ResponseCode.CLASS_FULL);
        }

        Booking booking = Booking.builder()
                .student(student)
                .classEntity(classEntity)
                .totalPrice(classEntity.getFee())
                .status(Booking.BookingStatus.PENDING)
                .build();

        booking = bookingRepository.save(booking);

        // Update class current students
        classEntity.setCurrentStudents(classEntity.getCurrentStudents() + 1);
        if (classEntity.getCurrentStudents().equals(classEntity.getMaxStudents())) {
            classEntity.setStatus(Class.ClassStatus.FULL);
        }
        classRepository.save(classEntity);

        log.info("Booking created: {}", booking.getId());
        return bookingMapper.toBookingResponse(booking);
    }

    @Transactional
    public BookingResponse confirmBooking(Long tutorId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new GeneralException(ResponseCode.BOOKING_NOT_FOUND));

        // Check if tutor owns the class
        if (!booking.getClassEntity().getTutor().getId().equals(tutorId)) {
            throw new GeneralException(ResponseCode.UNAUTHORIZED_BOOKING_ACCESS);
        }

        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        booking.setConfirmedAt(LocalDateTime.now());
        booking = bookingRepository.save(booking);

        log.info("Booking confirmed: {}", bookingId);
        return bookingMapper.toBookingResponse(booking);
    }

    @Transactional
    public BookingResponse cancelBooking(Long studentId, Long bookingId, String reason) {
        Booking booking = bookingRepository.findByIdAndStudentId(bookingId, studentId)
                .orElseThrow(() -> new GeneralException(ResponseCode.BOOKING_NOT_FOUND));

        if (booking.getStatus() == Booking.BookingStatus.COMPLETED ||
                booking.getStatus() == Booking.BookingStatus.CANCELLED) {
            throw new GeneralException(ResponseCode.BOOKING_CANNOT_CANCEL);
        }

        booking.setStatus(Booking.BookingStatus.CANCELLED);
        booking.setCancelledAt(LocalDateTime.now());
        booking.setCancellationReason(reason);
        booking = bookingRepository.save(booking);

        // Update class current students
        Class classEntity = booking.getClassEntity();
        classEntity.setCurrentStudents(classEntity.getCurrentStudents() - 1);
        if (classEntity.getStatus() == Class.ClassStatus.FULL) {
            classEntity.setStatus(Class.ClassStatus.ACTIVE);
        }
        classRepository.save(classEntity);

        log.info("Booking cancelled: {}", bookingId);
        return bookingMapper.toBookingResponse(booking);
    }

    public List<BookingResponse> getStudentBookings(Long studentId) {
        List<Booking> bookings = bookingRepository.findByStudentId(studentId);
        return bookingMapper.toBookingResponseList(bookings);
    }

    public List<BookingResponse> getTutorBookings(Long tutorId) {
        List<Booking> bookings = bookingRepository.findByClassEntityTutorId(tutorId);
        return bookingMapper.toBookingResponseList(bookings);
    }

    public BookingResponse getBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new GeneralException(ResponseCode.BOOKING_NOT_FOUND));
        return bookingMapper.toBookingResponse(booking);
    }
    public List<BookingResponse> getClassBookings(Long classId) {
        // Có thể thêm logic kiểm tra classId có tồn tại hay không nếu cần thiết
        List<Booking> bookings = bookingRepository.findByClassEntityId(classId);

        // Chỉ hiển thị các sinh viên đã được CONFIRMED hoặc PENDING (nếu muốn)
        List<Booking> confirmedBookings = bookings.stream()
                .filter(b -> b.getStatus() == Booking.BookingStatus.CONFIRMED || b.getStatus() == Booking.BookingStatus.PENDING)
                .toList();

        return bookingMapper.toBookingResponseList(confirmedBookings);
    }
}
