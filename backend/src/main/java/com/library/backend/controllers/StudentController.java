package com.library.backend.controllers;

import com.library.backend.dtos.requests.UpdateProfileRequest;
import com.library.backend.dtos.responses.ApiResponse;
import com.library.backend.dtos.responses.StudentProfileResponse;
import com.library.backend.services.StudentControl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StudentController {

    StudentControl studentControl;

    @GetMapping("/{studentId}/profile")
    public ResponseEntity<ApiResponse<StudentProfileResponse>> getProfile(@PathVariable Long studentId) {
        StudentProfileResponse response = studentControl.getProfile(studentId);
        return ResponseEntity.ok(ApiResponse.<StudentProfileResponse>builder()
                .data(response)
                .message("Student profile retrieved successfully")
                .build());
    }

    @PutMapping("/{studentId}/profile")
    public ResponseEntity<ApiResponse<StudentProfileResponse>> updateProfile(
            @PathVariable Long studentId,
            @RequestBody UpdateProfileRequest request) {
        StudentProfileResponse response = studentControl.updateProfile(studentId, request);
        return ResponseEntity.ok(ApiResponse.<StudentProfileResponse>builder()
                .data(response)
                .message("Student profile updated successfully")
                .build());
    }
}
