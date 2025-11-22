package com.library.backend.controllers;

import com.library.backend.dtos.requests.SearchTutorRequest;
import com.library.backend.dtos.requests.UpdateProfileRequest;
import com.library.backend.dtos.responses.ApiResponse;
import com.library.backend.dtos.responses.TutorProfileResponse;
import com.library.backend.services.TutorControl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tutors")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TutorController {

    TutorControl tutorControl;

    @GetMapping("/{tutorId}/profile")
    public ResponseEntity<ApiResponse<TutorProfileResponse>> getProfile(@PathVariable Long tutorId) {
        TutorProfileResponse response = tutorControl.getProfile(tutorId);
        return ResponseEntity.ok(ApiResponse.<TutorProfileResponse>builder()
                .data(response)
                .message("Tutor profile retrieved successfully")
                .build());
    }

    @PutMapping("/{tutorId}/profile")
    public ResponseEntity<ApiResponse<TutorProfileResponse>> updateProfile(
            @PathVariable Long tutorId,
            @RequestBody UpdateProfileRequest request) {
        TutorProfileResponse response = tutorControl.updateProfile(tutorId, request);
        return ResponseEntity.ok(ApiResponse.<TutorProfileResponse>builder()
                .data(response)
                .message("Tutor profile updated successfully")
                .build());
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<List<TutorProfileResponse>>> searchTutors(
            @RequestBody SearchTutorRequest request) {
        List<TutorProfileResponse> response = tutorControl.searchTutors(request);
        return ResponseEntity.ok(ApiResponse.<List<TutorProfileResponse>>builder()
                .data(response)
                .message("Tutors retrieved successfully")
                .build());
    }
}
