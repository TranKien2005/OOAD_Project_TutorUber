package com.library.backend.controllers;

import com.library.backend.dtos.requests.CreateBookingRequest;
import com.library.backend.dtos.responses.ApiResponse;
import com.library.backend.dtos.responses.BookingResponse;
import com.library.backend.services.BookingControl;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingController {

        BookingControl bookingControl;

        @PostMapping("/student/{studentId}")
        public ResponseEntity<ApiResponse<BookingResponse>> createBooking(
                        @PathVariable Long studentId,
                        @Valid @RequestBody CreateBookingRequest request) {
                BookingResponse response = bookingControl.createBooking(studentId, request);
                return ResponseEntity.ok(ApiResponse.<BookingResponse>builder()
                                .data(response)
                                .message("Booking created successfully")
                                .build());
        }

        @PutMapping("/{bookingId}/confirm/tutor/{tutorId}")
        public ResponseEntity<ApiResponse<BookingResponse>> confirmBooking(
                        @PathVariable Long tutorId,
                        @PathVariable Long bookingId) {
                BookingResponse response = bookingControl.confirmBooking(tutorId, bookingId);
                return ResponseEntity.ok(ApiResponse.<BookingResponse>builder()
                                .data(response)
                                .message("Booking confirmed successfully")
                                .build());
        }

        @PutMapping("/{bookingId}/cancel/student/{studentId}")
        public ResponseEntity<ApiResponse<BookingResponse>> cancelBooking(
                        @PathVariable Long studentId,
                        @PathVariable Long bookingId,
                        @RequestParam(required = false) String reason) {
                try {
                        BookingResponse response = bookingControl.cancelBooking(studentId, bookingId, reason);
                        return ResponseEntity.ok(ApiResponse.<BookingResponse>builder()
                                        .data(response)
                                        .message("Booking cancelled successfully")
                                        .build());
                } catch (IllegalArgumentException e) {
                        return ResponseEntity.badRequest().body(ApiResponse.<BookingResponse>builder()
                                        .message(e.getMessage())
                                        .build());
                } catch (Exception e) {
                        return ResponseEntity.status(500).body(ApiResponse.<BookingResponse>builder()
                                        .message("Internal error")
                                        .build());
                }
        }

        @GetMapping("/{bookingId}")
        public ResponseEntity<ApiResponse<BookingResponse>> getBooking(@PathVariable Long bookingId) {
                BookingResponse response = bookingControl.getBooking(bookingId);
                return ResponseEntity.ok(ApiResponse.<BookingResponse>builder()
                                .data(response)
                                .message("Booking retrieved successfully")
                                .build());
        }

        @GetMapping("/student/{studentId}")
        public ResponseEntity<ApiResponse<List<BookingResponse>>> getStudentBookings(@PathVariable Long studentId) {
                List<BookingResponse> response = bookingControl.getStudentBookings(studentId);
                return ResponseEntity.ok(ApiResponse.<List<BookingResponse>>builder()
                                .data(response)
                                .message("Student bookings retrieved successfully")
                                .build());
        }

        @GetMapping("/tutor/{tutorId}")
        public ResponseEntity<ApiResponse<List<BookingResponse>>> getTutorBookings(@PathVariable Long tutorId) {
                List<BookingResponse> response = bookingControl.getTutorBookings(tutorId);
                return ResponseEntity.ok(ApiResponse.<List<BookingResponse>>builder()
                                .data(response)
                                .message("Tutor bookings retrieved successfully")
                                .build());
        }

        @GetMapping("/class/{classId}")
        public ResponseEntity<ApiResponse<List<BookingResponse>>> getClassBookings(@PathVariable Long classId) {
                List<BookingResponse> response = bookingControl.getClassBookings(classId);
                return ResponseEntity.ok(ApiResponse.<List<BookingResponse>>builder()
                                .data(response)
                                .message("Class bookings retrieved successfully")
                                .build());
        }
}
