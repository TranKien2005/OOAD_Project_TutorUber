package com.library.backend.services;

import com.library.backend.dtos.requests.CreateClassRequest;
import com.library.backend.dtos.requests.UpdateClassRequest;
import com.library.backend.dtos.responses.ClassResponse;
import com.library.backend.entities.Class;
import com.library.backend.entities.Schedule;
import com.library.backend.entities.Tutor;
import com.library.backend.exceptions.GeneralException;
import com.library.backend.exceptions.ResponseCode;
import com.library.backend.mappers.ClassMapper;
import com.library.backend.repositories.ClassRepository;
import com.library.backend.repositories.ScheduleRepository;
import com.library.backend.repositories.TutorRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ClassControl {

    ClassRepository classRepository;
    TutorRepository tutorRepository;
    ScheduleRepository scheduleRepository;
    ClassMapper classMapper;

    @Transactional
    public ClassResponse createClass(Long tutorId, CreateClassRequest request) {
        Tutor tutor = tutorRepository.findById(tutorId)
                .orElseThrow(() -> new GeneralException(ResponseCode.TUTOR_NOT_FOUND));

        Class classEntity = Class.builder()
                .tutor(tutor)
                .subject(request.getSubject())
                .description(request.getDescription())
                .fee(request.getFee())
                .maxStudents(request.getMaxStudents())
                .currentStudents(0)
                .location(request.getLocation())
                .status(Class.ClassStatus.ACTIVE)
                .build();

        classEntity = classRepository.save(classEntity);

        // Create schedules
        if (request.getSchedules() != null) {
            for (CreateClassRequest.ScheduleRequest scheduleReq : request.getSchedules()) {
                Schedule schedule = Schedule.builder()
                        .classEntity(classEntity)
                        .dayOfWeek(scheduleReq.getDayOfWeek())
                        .startTime(LocalTime.parse(scheduleReq.getStartTime()))
                        .endTime(LocalTime.parse(scheduleReq.getEndTime()))
                        .location(scheduleReq.getLocation())
                        .build();
                scheduleRepository.save(schedule);
            }
        }

        log.info("Class created: {}", classEntity.getId());
        return classMapper.toClassResponse(classRepository.findById(classEntity.getId()).get());
    }

    @Transactional
    public ClassResponse updateClass(Long tutorId, Long classId, UpdateClassRequest request) {
        Class classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new GeneralException(ResponseCode.CLASS_NOT_FOUND));

        if (!classEntity.getTutor().getId().equals(tutorId)) {
            throw new GeneralException(ResponseCode.UNAUTHORIZED_CLASS_ACCESS);
        }

        if (request.getSubject() != null) {
            classEntity.setSubject(request.getSubject());
        }
        if (request.getDescription() != null) {
            classEntity.setDescription(request.getDescription());
        }
        if (request.getFee() != null) {
            classEntity.setFee(request.getFee());
        }
        if (request.getMaxStudents() != null) {
            classEntity.setMaxStudents(request.getMaxStudents());
        }
        if (request.getLocation() != null) {
            classEntity.setLocation(request.getLocation());
        }
        if (request.getStatus() != null) {
            classEntity.setStatus(Class.ClassStatus.valueOf(request.getStatus()));
        }

        classEntity = classRepository.save(classEntity);

        // Update schedules if provided
        if (request.getSchedules() != null) {
            // Delete old schedules
            scheduleRepository.deleteAll(scheduleRepository.findByClassEntityId(classId));

            // Create new schedules
            for (CreateClassRequest.ScheduleRequest scheduleReq : request.getSchedules()) {
                Schedule schedule = Schedule.builder()
                        .classEntity(classEntity)
                        .dayOfWeek(scheduleReq.getDayOfWeek())
                        .startTime(LocalTime.parse(scheduleReq.getStartTime()))
                        .endTime(LocalTime.parse(scheduleReq.getEndTime()))
                        .location(scheduleReq.getLocation())
                        .build();
                scheduleRepository.save(schedule);
            }
        }

        log.info("Class updated: {}", classId);
        return classMapper.toClassResponse(classRepository.findById(classId).get());
    }

    @Transactional
    public void deleteClass(Long tutorId, Long classId) {
        Class classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new GeneralException(ResponseCode.CLASS_NOT_FOUND));

        if (!classEntity.getTutor().getId().equals(tutorId)) {
            throw new GeneralException(ResponseCode.UNAUTHORIZED_CLASS_ACCESS);
        }

        classRepository.delete(classEntity);
        log.info("Class deleted: {}", classId);
    }

    public ClassResponse getClass(Long classId) {
        Class classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new GeneralException(ResponseCode.CLASS_NOT_FOUND));

        return classMapper.toClassResponse(classEntity);
    }

    public List<ClassResponse> getTutorClasses(Long tutorId) {
        List<Class> classes = classRepository.findByTutorId(tutorId);
        return classMapper.toClassResponseList(classes);
    }

    public List<ClassResponse> getAllActiveClasses() {
        List<Class> classes = classRepository.findByStatus(Class.ClassStatus.ACTIVE);
        return classMapper.toClassResponseList(classes);
    }
}
