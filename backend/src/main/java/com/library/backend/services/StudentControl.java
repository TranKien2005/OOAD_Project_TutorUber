package com.library.backend.services;

import com.library.backend.dtos.requests.UpdateProfileRequest;
import com.library.backend.dtos.responses.StudentProfileResponse;
import com.library.backend.entities.Student;
import com.library.backend.entities.StudentProfile;
import com.library.backend.exceptions.GeneralException;
import com.library.backend.exceptions.ResponseCode;
import com.library.backend.mappers.StudentMapper;
import com.library.backend.repositories.StudentProfileRepository;
import com.library.backend.repositories.StudentRepository;
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
public class StudentControl {

    StudentRepository studentRepository;
    StudentProfileRepository studentProfileRepository;
    StudentMapper studentMapper;

    public StudentProfileResponse getProfile(Long studentId) {
        studentRepository.findById(studentId)
                .orElseThrow(() -> new GeneralException(ResponseCode.STUDENT_NOT_FOUND));

        StudentProfile profile = studentProfileRepository.findByStudentId(studentId)
                .orElseThrow(() -> new GeneralException(ResponseCode.STUDENT_PROFILE_NOT_FOUND));

        return studentMapper.toStudentProfileResponse(profile);
    }

    @Transactional
    public StudentProfileResponse updateProfile(Long studentId, UpdateProfileRequest request) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new GeneralException(ResponseCode.STUDENT_NOT_FOUND));

        // Update user info
        if (request.getFullName() != null) {
            student.setFullName(request.getFullName());
        }
        if (request.getEmail() != null) {
            student.setEmail(request.getEmail());
        }
        if (request.getPhone() != null) {
            student.setPhone(request.getPhone());
        }
        studentRepository.save(student);

        // Update profile
        StudentProfile profile = studentProfileRepository.findByStudentId(studentId)
                .orElseThrow(() -> new GeneralException(ResponseCode.STUDENT_PROFILE_NOT_FOUND));

        if (request.getAvatar() != null) {
            profile.setAvatar(request.getAvatar());
        }
        if (request.getBio() != null) {
            profile.setBio(request.getBio());
        }
        if (request.getPreferenceSubjects() != null) {
            profile.setPreferenceSubjects(request.getPreferenceSubjects());
        }
        if (request.getBudgetMin() != null) {
            profile.setBudgetMin(request.getBudgetMin());
        }
        if (request.getBudgetMax() != null) {
            profile.setBudgetMax(request.getBudgetMax());
        }
        if (request.getLocation() != null) {
            profile.setLocation(request.getLocation());
        }

        profile = studentProfileRepository.save(profile);

        log.info("Student profile updated: {}", studentId);
        return studentMapper.toStudentProfileResponse(profile);
    }
}
