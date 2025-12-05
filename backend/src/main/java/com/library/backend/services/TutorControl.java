package com.library.backend.services;

import com.library.backend.dtos.requests.SearchTutorRequest;
import com.library.backend.dtos.requests.UpdateProfileRequest;
import com.library.backend.dtos.responses.TutorProfileResponse;
import com.library.backend.entities.Tutor;
import com.library.backend.entities.TutorProfile;
import com.library.backend.exceptions.GeneralException;
import com.library.backend.exceptions.ResponseCode;
import com.library.backend.mappers.TutorMapper;
import com.library.backend.repositories.TutorProfileRepository;
import com.library.backend.repositories.TutorRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TutorControl {

    TutorRepository tutorRepository;
    TutorProfileRepository tutorProfileRepository;
    TutorMapper tutorMapper;

    public TutorProfileResponse getProfile(Long tutorId) {
        tutorRepository.findById(tutorId)
                .orElseThrow(() -> new GeneralException(ResponseCode.TUTOR_NOT_FOUND));

        TutorProfile profile = tutorProfileRepository.findByTutorId(tutorId)
                .orElseThrow(() -> new GeneralException(ResponseCode.TUTOR_PROFILE_NOT_FOUND));

        return tutorMapper.toTutorProfileResponse(profile);
    }

    @Transactional
    public TutorProfileResponse updateProfile(Long tutorId, UpdateProfileRequest request) {
        Tutor tutor = tutorRepository.findById(tutorId)
                .orElseThrow(() -> new GeneralException(ResponseCode.TUTOR_NOT_FOUND));

        // Update user info
        if (request.getFullName() != null) {
            tutor.setFullName(request.getFullName());
        }
        if (request.getEmail() != null) {
            tutor.setEmail(request.getEmail());
        }
        if (request.getPhone() != null) {
            tutor.setPhone(request.getPhone());
        }
        tutorRepository.save(tutor);

        // Update profile
        TutorProfile profile = tutorProfileRepository.findByTutorId(tutorId)
                .orElseThrow(() -> new GeneralException(ResponseCode.TUTOR_PROFILE_NOT_FOUND));

        if (request.getAvatar() != null) {
            profile.setAvatar(request.getAvatar());
        }
        if (request.getBio() != null) {
            profile.setBio(request.getBio());
        }
        if (request.getHourlyRate() != null) {
            profile.setHourlyRate(request.getHourlyRate());
        }
        if (request.getSpecialization() != null) {
            profile.setSpecialization(request.getSpecialization());
        }
        if (request.getEducation() != null) {
            profile.setEducation(request.getEducation());
        }
        if (request.getYearsOfExperience() != null) {
            profile.setYearsOfExperience(request.getYearsOfExperience());
        }

        profile = tutorProfileRepository.save(profile);

        log.info("Tutor profile updated: {}", tutorId);
        return tutorMapper.toTutorProfileResponse(profile);
    }

    public List<TutorProfileResponse> searchTutors(SearchTutorRequest request) {
        List<TutorProfile> profiles = tutorProfileRepository.findAll();

        // Simple filtering (can be improved with Specification)
        return profiles.stream()
                .filter(p -> request.getMinRate() == null || p.getHourlyRate() >= request.getMinRate())
                .filter(p -> request.getMaxRate() == null || p.getHourlyRate() <= request.getMaxRate())
                .filter(p -> request.getMinRating() == null || p.getRating() >= request.getMinRating())
                .filter(p -> request.getSpecialization() == null || 
                        (p.getSpecialization() != null && p.getSpecialization().contains(request.getSpecialization())))
                .filter(p -> request.getVerificationStatus() == null || 
                        p.getVerificationStatus().name().equals(request.getVerificationStatus()))
                .map(tutorMapper::toTutorProfileResponse)
                .collect(Collectors.toList());
    }
}
