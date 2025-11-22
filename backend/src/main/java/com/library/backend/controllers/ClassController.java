package com.library.backend.controllers;

import com.library.backend.dtos.requests.CreateClassRequest;
import com.library.backend.dtos.requests.UpdateClassRequest;
import com.library.backend.dtos.responses.ApiResponse;
import com.library.backend.dtos.responses.ClassResponse;
import com.library.backend.services.ClassControl;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/classes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClassController {

    ClassControl classControl;

    @PostMapping("/tutor/{tutorId}")
    public ResponseEntity<ApiResponse<ClassResponse>> createClass(
            @PathVariable Long tutorId,
            @Valid @RequestBody CreateClassRequest request) {
        ClassResponse response = classControl.createClass(tutorId, request);
        return ResponseEntity.ok(ApiResponse.<ClassResponse>builder()
                .data(response)
                .message("Class created successfully")
                .build());
    }

    @PutMapping("/{classId}/tutor/{tutorId}")
    public ResponseEntity<ApiResponse<ClassResponse>> updateClass(
            @PathVariable Long tutorId,
            @PathVariable Long classId,
            @RequestBody UpdateClassRequest request) {
        ClassResponse response = classControl.updateClass(tutorId, classId, request);
        return ResponseEntity.ok(ApiResponse.<ClassResponse>builder()
                .data(response)
                .message("Class updated successfully")
                .build());
    }

    @DeleteMapping("/{classId}/tutor/{tutorId}")
    public ResponseEntity<ApiResponse<Void>> deleteClass(
            @PathVariable Long tutorId,
            @PathVariable Long classId) {
        classControl.deleteClass(tutorId, classId);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .message("Class deleted successfully")
                .build());
    }

    @GetMapping("/{classId}")
    public ResponseEntity<ApiResponse<ClassResponse>> getClass(@PathVariable Long classId) {
        ClassResponse response = classControl.getClass(classId);
        return ResponseEntity.ok(ApiResponse.<ClassResponse>builder()
                .data(response)
                .message("Class retrieved successfully")
                .build());
    }

    @GetMapping("/tutor/{tutorId}")
    public ResponseEntity<ApiResponse<List<ClassResponse>>> getTutorClasses(@PathVariable Long tutorId) {
        List<ClassResponse> response = classControl.getTutorClasses(tutorId);
        return ResponseEntity.ok(ApiResponse.<List<ClassResponse>>builder()
                .data(response)
                .message("Tutor classes retrieved successfully")
                .build());
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<ClassResponse>>> getAllActiveClasses() {
        List<ClassResponse> response = classControl.getAllActiveClasses();
        return ResponseEntity.ok(ApiResponse.<List<ClassResponse>>builder()
                .data(response)
                .message("Active classes retrieved successfully")
                .build());
    }
}
