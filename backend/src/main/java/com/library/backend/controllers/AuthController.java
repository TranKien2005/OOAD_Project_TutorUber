package com.library.backend.controllers;

import com.library.backend.dtos.requests.ChangePasswordRequest;
import com.library.backend.dtos.requests.LoginRequest;
import com.library.backend.dtos.requests.RegisterRequest;
import com.library.backend.dtos.responses.ApiResponse;
import com.library.backend.dtos.responses.AuthResponse;
import com.library.backend.dtos.responses.UserResponse;
import com.library.backend.services.AuthControl;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

    AuthControl authControl;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse response = authControl.register(request);
        return ResponseEntity.ok(ApiResponse.<UserResponse>builder()
                .success(true)
                .data(response)
                .message("User registered successfully")
                .build());
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authControl.login(request);
        return ResponseEntity.ok(ApiResponse.<AuthResponse>builder()
                .success(true)
                .data(response)
                .message("Login successful")
                .build());
    }

    @PutMapping("/change-password/{userId}")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @PathVariable Long userId,
            @Valid @RequestBody ChangePasswordRequest request) {
        authControl.changePassword(userId, request);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Password changed successfully")
                .build());
    }
}
