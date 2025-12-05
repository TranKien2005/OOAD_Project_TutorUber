package com.library.backend.controllers;

import com.library.backend.dtos.requests.CreateRatingRequest;
import com.library.backend.dtos.responses.ApiResponse;
import com.library.backend.dtos.responses.RatingResponse;
import com.library.backend.services.RatingControl;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ratings")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RatingController {

    RatingControl ratingControl;

    @PostMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<RatingResponse>> createRating(
            @PathVariable Long studentId,
            @Valid @RequestBody CreateRatingRequest request) {
        RatingResponse response = ratingControl.createRating(studentId, request);
        return ResponseEntity.ok(ApiResponse.<RatingResponse>builder()
                .data(response)
                .message("Rating created successfully")
                .build());
    }

    @GetMapping("/tutor/{tutorId}")
    public ResponseEntity<ApiResponse<List<RatingResponse>>> getTutorRatings(@PathVariable Long tutorId) {
        List<RatingResponse> response = ratingControl.getTutorRatings(tutorId);
        return ResponseEntity.ok(ApiResponse.<List<RatingResponse>>builder()
                .data(response)
                .message("Tutor ratings retrieved successfully")
                .build());
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<RatingResponse>>> getStudentRatings(@PathVariable Long studentId) {
        List<RatingResponse> response = ratingControl.getStudentRatings(studentId);
        return ResponseEntity.ok(ApiResponse.<List<RatingResponse>>builder()
                .data(response)
                .message("Student ratings retrieved successfully")
                .build());
    }
}
