package com.library.backend.services;

import com.library.backend.dtos.requests.CreateRatingRequest;
import com.library.backend.dtos.responses.RatingResponse;
import com.library.backend.entities.Booking;
import com.library.backend.entities.Rating;
import com.library.backend.entities.Student;
import com.library.backend.entities.TutorProfile;
import com.library.backend.exceptions.GeneralException;
import com.library.backend.exceptions.ResponseCode;
import com.library.backend.mappers.RatingMapper;
import com.library.backend.repositories.BookingRepository;
import com.library.backend.repositories.RatingRepository;
import com.library.backend.repositories.StudentRepository;
import com.library.backend.repositories.TutorProfileRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RatingControl {

    RatingRepository ratingRepository;
    BookingRepository bookingRepository;
    StudentRepository studentRepository;
    TutorProfileRepository tutorProfileRepository;
    RatingMapper ratingMapper;

    @Transactional
    public RatingResponse createRating(Long studentId, CreateRatingRequest request) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new GeneralException(ResponseCode.STUDENT_NOT_FOUND));

        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new GeneralException(ResponseCode.BOOKING_NOT_FOUND));

        // Check if booking belongs to student
        if (!booking.getStudent().getId().equals(studentId)) {
            throw new GeneralException(ResponseCode.UNAUTHORIZED_BOOKING_ACCESS);
        }

        // Check if booking is completed
        if (booking.getStatus() != Booking.BookingStatus.COMPLETED) {
            throw new GeneralException(ResponseCode.BOOKING_NOT_COMPLETED);
        }

        // Check if rating already exists
        if (ratingRepository.existsByBookingId(request.getBookingId())) {
            throw new GeneralException(ResponseCode.RATING_ALREADY_EXISTS);
        }

        Rating rating = Rating.builder()
                .tutor(booking.getClassEntity().getTutor())
                .student(student)
                .booking(booking)
                .score(request.getScore())
                .comment(request.getComment())
                .build();

        rating = ratingRepository.save(rating);

        // Update tutor rating
        updateTutorRating(booking.getClassEntity().getTutor().getId());

        log.info("Rating created: {}", rating.getId());
        return ratingMapper.toRatingResponse(rating);
    }

    private void updateTutorRating(Long tutorId) {
        List<Rating> ratings = ratingRepository.findByTutorId(tutorId);
        if (!ratings.isEmpty()) {
            double avgRating = ratings.stream()
                    .mapToInt(Rating::getScore)
                    .average()
                    .orElse(0.0);

            TutorProfile profile = tutorProfileRepository.findByTutorId(tutorId)
                    .orElseThrow(() -> new GeneralException(ResponseCode.TUTOR_PROFILE_NOT_FOUND));

            profile.setRating(avgRating);
            profile.setTotalRatings(ratings.size());
            tutorProfileRepository.save(profile);
        }
    }

    public List<RatingResponse> getTutorRatings(Long tutorId) {
        List<Rating> ratings = ratingRepository.findByTutorId(tutorId);
        return ratingMapper.toRatingResponseList(ratings);
    }

    public List<RatingResponse> getStudentRatings(Long studentId) {
        List<Rating> ratings = ratingRepository.findByStudentId(studentId);
        return ratingMapper.toRatingResponseList(ratings);
    }
}
