package com.library.backend.configurations;

import com.library.backend.entities.*;
import com.library.backend.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalTime;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class InitConfig {

        private final UserRepository userRepository;
        private final StudentRepository studentRepository;
        private final TutorRepository tutorRepository;
        private final AdminRepository adminRepository;
        private final StudentProfileRepository studentProfileRepository;
        private final TutorProfileRepository tutorProfileRepository;
        private final ClassRepository classRepository;
        private final ScheduleRepository scheduleRepository;
        private final BookingRepository bookingRepository;
        private final RatingRepository ratingRepository;
        private final CertificateRepository certificateRepository;

        @Bean
        CommandLineRunner initData() {
                return args -> {
                        if (userRepository.count() > 0) {
                                log.info("Database already initialized, skipping data seeding");
                                return;
                        }

                        log.info("Starting database initialization with sample data...");

                        // Create Admin
                        Admin admin = new Admin();
                        admin.setUsername("admin");
                        admin.setPassword("admin123");
                        admin.setEmail("admin@tutoruber.com");
                        admin.setFullName("System Administrator");
                        admin.setPhone("0123456789");
                        admin.setRole(User.UserRole.ADMIN);
                        admin.setActive(true);
                        adminRepository.save(admin);
                        log.info("Created admin user");

                        // Create Students
                        Student student1 = new Student();
                        student1.setUsername("student1");
                        student1.setPassword("password123");
                        student1.setEmail("student1@example.com");
                        student1.setFullName("Nguyen Van A");
                        student1.setPhone("0987654321");
                        student1.setRole(User.UserRole.STUDENT);
                        student1.setActive(true);

                        Student student2 = new Student();
                        student2.setUsername("student2");
                        student2.setPassword("password123");
                        student2.setEmail("student2@example.com");
                        student2.setFullName("Tran Thi B");
                        student2.setPhone("0987654322");
                        student2.setRole(User.UserRole.STUDENT);
                        student2.setActive(true);

                        Student student3 = new Student();
                        student3.setUsername("student3");
                        student3.setPassword("password123");
                        student3.setEmail("student3@example.com");
                        student3.setFullName("Le Van C");
                        student3.setPhone("0987654323");
                        student3.setRole(User.UserRole.STUDENT);
                        student3.setActive(true);

                        studentRepository.saveAll(List.of(student1, student2, student3));
                        log.info("Created 3 students");

                        // Create Student Profiles
                        StudentProfile studentProfile1 = StudentProfile.builder()
                                        .student(student1)
                                        .preferenceSubjects("Mathematics, Physics")
                                        .budgetMin(150000.0)
                                        .budgetMax(250000.0)
                                        .location("District 1, HCMC")
                                        .bio("Grade 12 student at Le Hong Phong High School")
                                        .build();

                        StudentProfile studentProfile2 = StudentProfile.builder()
                                        .student(student2)
                                        .preferenceSubjects("English, Chemistry")
                                        .budgetMin(100000.0)
                                        .budgetMax(200000.0)
                                        .location("District 3, HCMC")
                                        .bio("Grade 11 student at Tran Dai Nghia High School")
                                        .build();

                        StudentProfile studentProfile3 = StudentProfile.builder()
                                        .student(student3)
                                        .preferenceSubjects("Biology, Literature")
                                        .budgetMin(80000.0)
                                        .budgetMax(150000.0)
                                        .location("District 5, HCMC")
                                        .bio("Grade 10 student at Nguyen Thi Minh Khai High School")
                                        .build();

                        studentProfileRepository.saveAll(List.of(studentProfile1, studentProfile2, studentProfile3));
                        log.info("Created 3 student profiles");

                        // Create Tutors
                        Tutor tutor1 = new Tutor();
                        tutor1.setUsername("tutor1");
                        tutor1.setPassword("password123");
                        tutor1.setEmail("tutor1@example.com");
                        tutor1.setFullName("Dr. Pham Van D");
                        tutor1.setPhone("0912345671");
                        tutor1.setRole(User.UserRole.TUTOR);
                        tutor1.setActive(true);

                        Tutor tutor2 = new Tutor();
                        tutor2.setUsername("tutor2");
                        tutor2.setPassword("password123");
                        tutor2.setEmail("tutor2@example.com");
                        tutor2.setFullName("Ms. Hoang Thi E");
                        tutor2.setPhone("0912345672");
                        tutor2.setRole(User.UserRole.TUTOR);
                        tutor2.setActive(true);

                        Tutor tutor3 = new Tutor();
                        tutor3.setUsername("tutor3");
                        tutor3.setPassword("password123");
                        tutor3.setEmail("tutor3@example.com");
                        tutor3.setFullName("Mr. Vo Van F");
                        tutor3.setPhone("0912345673");
                        tutor3.setRole(User.UserRole.TUTOR);
                        tutor3.setActive(true);

                        tutorRepository.saveAll(List.of(tutor1, tutor2, tutor3));
                        log.info("Created 3 tutors");

                        // Create Tutor Profiles
                        TutorProfile tutorProfile1 = TutorProfile.builder()
                                        .tutor(tutor1)
                                        .bio("10 years of experience teaching Mathematics and Physics for high school students. PhD in Applied Mathematics.")
                                        .education("PhD in Applied Mathematics - HCMUS")
                                        .yearsOfExperience(10)
                                        .specialization("Mathematics, Physics")
                                        .hourlyRate(250000.0)
                                        .rating(4.8)
                                        .totalRatings(45)
                                        .verificationStatus(TutorProfile.VerificationStatus.APPROVED)
                                        .build();

                        TutorProfile tutorProfile2 = TutorProfile.builder()
                                        .tutor(tutor2)
                                        .bio("IELTS 8.5, 5 years teaching English for students preparing for international exams.")
                                        .education("Master in English Linguistics - HCMUTE")
                                        .yearsOfExperience(5)
                                        .specialization("English, IELTS")
                                        .hourlyRate(180000.0)
                                        .rating(4.9)
                                        .totalRatings(38)
                                        .verificationStatus(TutorProfile.VerificationStatus.APPROVED)
                                        .build();

                        TutorProfile tutorProfile3 = TutorProfile.builder()
                                        .tutor(tutor3)
                                        .bio("Chemistry teacher with passion for making science fun and easy to understand.")
                                        .education("Bachelor in Chemistry - VNUHCM")
                                        .yearsOfExperience(3)
                                        .specialization("Chemistry, Biology")
                                        .hourlyRate(120000.0)
                                        .rating(4.5)
                                        .totalRatings(22)
                                        .verificationStatus(TutorProfile.VerificationStatus.PENDING)
                                        .build();

                        tutorProfileRepository.saveAll(List.of(tutorProfile1, tutorProfile2, tutorProfile3));
                        log.info("Created 3 tutor profiles");

                        // Create Certificates for verified tutors
                        Certificate cert1 = Certificate.builder()
                                        .tutor(tutor1)
                                        .title("PhD in Applied Mathematics")
                                        .issuedBy("HCMUS")
                                        .issuedDate(java.time.LocalDate.of(2015, 6, 1))
                                        .verified(true)
                                        .build();

                        Certificate cert2 = Certificate.builder()
                                        .tutor(tutor2)
                                        .title("IELTS Certificate - 8.5")
                                        .issuedBy("British Council")
                                        .issuedDate(java.time.LocalDate.of(2020, 3, 15))
                                        .verified(true)
                                        .build();

                        certificateRepository.saveAll(List.of(cert1, cert2));
                        log.info("Created certificates for tutors");

                        // Create Classes
                        com.library.backend.entities.Class class1 = com.library.backend.entities.Class.builder()
                                        .tutor(tutor1)
                                        .description("Intensive preparation for university entrance exam. Focus on calculus, algebra, and geometry.")
                                        .subject("Mathematics")
                                        .fee(200000.0)
                                        .maxStudents(10)
                                        .currentStudents(0)
                                        .status(com.library.backend.entities.Class.ClassStatus.ACTIVE)
                                        .build();

                        com.library.backend.entities.Class class2 = com.library.backend.entities.Class.builder()
                                        .tutor(tutor1)
                                        .description("Comprehensive physics course covering mechanics, thermodynamics, and electromagnetism.")
                                        .subject("Physics")
                                        .fee(180000.0)
                                        .maxStudents(8)
                                        .currentStudents(0)
                                        .status(com.library.backend.entities.Class.ClassStatus.ACTIVE)
                                        .build();

                        com.library.backend.entities.Class class3 = com.library.backend.entities.Class.builder()
                                        .tutor(tutor2)
                                        .description("Complete IELTS preparation covering all four skills: Listening, Reading, Writing, Speaking.")
                                        .subject("English")
                                        .fee(150000.0)
                                        .maxStudents(12)
                                        .currentStudents(0)
                                        .status(com.library.backend.entities.Class.ClassStatus.ACTIVE)
                                        .build();

                        com.library.backend.entities.Class class4 = com.library.backend.entities.Class.builder()
                                        .tutor(tutor3)
                                        .description("Foundation chemistry course for high school students. Fun and interactive lessons.")
                                        .subject("Chemistry")
                                        .fee(100000.0)
                                        .maxStudents(15)
                                        .currentStudents(0)
                                        .status(com.library.backend.entities.Class.ClassStatus.ACTIVE)
                                        .build();

                        classRepository.saveAll(List.of(class1, class2, class3, class4));
                        log.info("Created 4 classes");

                        // Create Schedules for classes
                        Schedule schedule1a = Schedule.builder()
                                        .classEntity(class1)
                                        .dayOfWeek("MONDAY")
                                        .startTime(LocalTime.of(18, 0))
                                        .endTime(LocalTime.of(20, 0))
                                        .build();

                        Schedule schedule1b = Schedule.builder()
                                        .classEntity(class1)
                                        .dayOfWeek("THURSDAY")
                                        .startTime(LocalTime.of(18, 0))
                                        .endTime(LocalTime.of(20, 0))
                                        .build();

                        Schedule schedule2 = Schedule.builder()
                                        .classEntity(class2)
                                        .dayOfWeek("TUESDAY")
                                        .startTime(LocalTime.of(19, 0))
                                        .endTime(LocalTime.of(21, 0))
                                        .build();

                        Schedule schedule3a = Schedule.builder()
                                        .classEntity(class3)
                                        .dayOfWeek("WEDNESDAY")
                                        .startTime(LocalTime.of(17, 30))
                                        .endTime(LocalTime.of(19, 30))
                                        .build();

                        Schedule schedule3b = Schedule.builder()
                                        .classEntity(class3)
                                        .dayOfWeek("SATURDAY")
                                        .startTime(LocalTime.of(9, 0))
                                        .endTime(LocalTime.of(11, 0))
                                        .build();

                        Schedule schedule4 = Schedule.builder()
                                        .classEntity(class4)
                                        .dayOfWeek("FRIDAY")
                                        .startTime(LocalTime.of(18, 30))
                                        .endTime(LocalTime.of(20, 30))
                                        .build();

                        scheduleRepository.saveAll(
                                        List.of(schedule1a, schedule1b, schedule2, schedule3a, schedule3b, schedule4));
                        log.info("Created schedules for classes");

                        // Create Bookings
                        Booking booking1 = Booking.builder()
                                        .student(student1)
                                        .classEntity(class1)
                                        .status(Booking.BookingStatus.CONFIRMED)
                                        .totalPrice(200000.0)
                                        .build();

                        Booking booking2 = Booking.builder()
                                        .student(student2)
                                        .classEntity(class1)
                                        .status(Booking.BookingStatus.CONFIRMED)
                                        .totalPrice(200000.0)
                                        .build();

                        Booking booking3 = Booking.builder()
                                        .student(student3)
                                        .classEntity(class2)
                                        .status(Booking.BookingStatus.CONFIRMED)
                                        .totalPrice(180000.0)
                                        .build();

                        Booking booking4 = Booking.builder()
                                        .student(student1)
                                        .classEntity(class3)
                                        .status(Booking.BookingStatus.CONFIRMED)
                                        .totalPrice(150000.0)
                                        .build();

                        Booking booking5 = Booking.builder()
                                        .student(student2)
                                        .classEntity(class3)
                                        .status(Booking.BookingStatus.CONFIRMED)
                                        .totalPrice(150000.0)
                                        .build();

                        Booking booking6 = Booking.builder()
                                        .student(student3)
                                        .classEntity(class3)
                                        .status(Booking.BookingStatus.PENDING)
                                        .totalPrice(150000.0)
                                        .build();

                        bookingRepository.saveAll(List.of(booking1, booking2, booking3, booking4, booking5, booking6));
                        log.info("Created 6 bookings");

                        // Create Ratings for completed bookings
                        Rating rating1 = Rating.builder()
                                        .student(student1)
                                        .tutor(tutor1)
                                        .booking(booking1)
                                        .score(5)
                                        .comment("Excellent teacher! Very clear explanations and patient with students.")
                                        .build();

                        Rating rating2 = Rating.builder()
                                        .student(student2)
                                        .tutor(tutor1)
                                        .booking(booking2)
                                        .score(5)
                                        .comment("Best math tutor I've ever had. Highly recommended!")
                                        .build();

                        Rating rating3 = Rating.builder()
                                        .student(student3)
                                        .tutor(tutor1)
                                        .booking(booking3)
                                        .score(4)
                                        .comment("Good physics lessons, but sometimes goes too fast.")
                                        .build();

                        Rating rating4 = Rating.builder()
                                        .student(student1)
                                        .tutor(tutor2)
                                        .booking(booking4)
                                        .score(5)
                                        .comment("Amazing IELTS prep! Improved my score significantly.")
                                        .build();

                        ratingRepository.saveAll(List.of(rating1, rating2, rating3, rating4));
                        log.info("Created 4 ratings");

                        log.info("Database initialization completed successfully!");
                        log.info("Sample accounts:");
                        log.info("  Admin - username: admin, password: admin123");
                        log.info("  Students - username: student1/student2/student3, password: password123");
                        log.info("  Tutors - username: tutor1/tutor2/tutor3, password: password123");
                };
        }
}
