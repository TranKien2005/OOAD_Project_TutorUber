package com.library.backend.services;

import com.library.backend.dtos.requests.ChangePasswordRequest;
import com.library.backend.dtos.requests.LoginRequest;
import com.library.backend.dtos.requests.RegisterRequest;
import com.library.backend.dtos.responses.AuthResponse;
import com.library.backend.dtos.responses.UserResponse;
import com.library.backend.entities.*;
import com.library.backend.exceptions.GeneralException;
import com.library.backend.exceptions.ResponseCode;
import com.library.backend.mappers.AuthMapper;
import com.library.backend.repositories.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthControl {

    UserRepository userRepository;
    StudentRepository studentRepository;
    TutorRepository tutorRepository;
    AdminRepository adminRepository;
    StudentProfileRepository studentProfileRepository;
    TutorProfileRepository tutorProfileRepository;
    AuthMapper authMapper;

    @Transactional
    public UserResponse register(RegisterRequest request) {
        // Validate username and email
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new GeneralException(ResponseCode.USERNAME_EXISTED);
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new GeneralException(ResponseCode.EMAIL_EXISTED);
        }

        // Create user based on role
        User user;
        User.UserRole role = User.UserRole.valueOf(request.getRole().toUpperCase());

        switch (role) {
            case STUDENT -> {
                Student student = new Student();
                student.setUsername(request.getUsername());
                student.setPassword(request.getPassword()); // Simple password, no encoding
                student.setFullName(request.getFullName());
                student.setEmail(request.getEmail());
                student.setPhone(request.getPhone());
                student.setRole(User.UserRole.STUDENT);
                student.setActive(true);
                
                student = studentRepository.save(student);
                
                // Create student profile
                StudentProfile profile = StudentProfile.builder()
                        .student(student)
                        .build();
                studentProfileRepository.save(profile);
                
                user = student;
            }
            case TUTOR -> {
                Tutor tutor = new Tutor();
                tutor.setUsername(request.getUsername());
                tutor.setPassword(request.getPassword()); // Simple password, no encoding
                tutor.setFullName(request.getFullName());
                tutor.setEmail(request.getEmail());
                tutor.setPhone(request.getPhone());
                tutor.setRole(User.UserRole.TUTOR);
                tutor.setActive(true);
                
                tutor = tutorRepository.save(tutor);
                
                // Create tutor profile
                TutorProfile profile = TutorProfile.builder()
                        .tutor(tutor)
                        .build();
                tutorProfileRepository.save(profile);
                
                user = tutor;
            }
            case ADMIN -> {
                Admin admin = new Admin();
                admin.setUsername(request.getUsername());
                admin.setPassword(request.getPassword()); // Simple password, no encoding
                admin.setFullName(request.getFullName());
                admin.setEmail(request.getEmail());
                admin.setPhone(request.getPhone());
                admin.setRole(User.UserRole.ADMIN);
                admin.setActive(true);
                
                user = adminRepository.save(admin);
            }
            default -> throw new GeneralException(ResponseCode.INVALID_ROLE);
        }

        log.info("User registered successfully: {}", user.getUsername());
        return authMapper.toUserResponse(user);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new GeneralException(ResponseCode.INVALID_CREDENTIALS));

        if (!request.getPassword().equals(user.getPassword())) {
            throw new GeneralException(ResponseCode.INVALID_CREDENTIALS);
        }

        if (!user.getActive()) {
            throw new GeneralException(ResponseCode.ACCOUNT_DISABLED);
        }

        log.info("User logged in successfully: {}", user.getUsername());
        
        return AuthResponse.builder()
                .token("simple-token-" + user.getId()) // Simple token
                .user(authMapper.toUserResponse(user))
                .build();
    }

    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ResponseCode.USER_NOT_FOUND));

        // Verify current password
        if (!request.getCurrentPassword().equals(user.getPassword())) {
            throw new GeneralException(ResponseCode.INVALID_CREDENTIALS);
        }

        // Validate new password matches confirm password
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new GeneralException(ResponseCode.PASSWORD_MISMATCH);
        }

        // Update password
        user.setPassword(request.getNewPassword());
        userRepository.save(user);

        log.info("Password changed successfully for user: {}", user.getUsername());
    }
}
