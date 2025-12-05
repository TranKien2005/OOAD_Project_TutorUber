package com.library.backend.exceptions;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public enum ResponseCode {

    // User & Auth related
    USER_NOT_FOUND("User does not exist in the system."),
    USERNAME_EXISTED("Username already exists."),
    EMAIL_EXISTED("Email already exists."),
    INVALID_CREDENTIALS("Invalid username or password."),
    INVALID_ROLE("Invalid user role."),
    ACCOUNT_DISABLED("Account is disabled."),
    
    // Student related
    STUDENT_NOT_FOUND("Student does not exist in the system."),
    STUDENT_PROFILE_NOT_FOUND("Student profile not found."),
    
    // Tutor related
    TUTOR_NOT_FOUND("Tutor does not exist in the system."),
    TUTOR_PROFILE_NOT_FOUND("Tutor profile not found."),
    
    // Class related
    CLASS_NOT_FOUND("Class does not exist in the system."),
    CLASS_FULL("Class is full."),
    CLASS_NOT_ACTIVE("Class is not active."),
    UNAUTHORIZED_CLASS_ACCESS("You are not authorized to access this class."),
    
    // Booking related
    BOOKING_NOT_FOUND("Booking does not exist in the system."),
    BOOKING_ALREADY_EXISTS("Booking already exists for this class."),
    BOOKING_CANNOT_CANCEL("Booking cannot be cancelled."),
    UNAUTHORIZED_BOOKING_ACCESS("You are not authorized to access this booking."),
    
    // Rating related
    RATING_NOT_FOUND("Rating does not exist in the system."),
    RATING_ALREADY_EXISTS("Rating already exists for this booking."),
    BOOKING_NOT_COMPLETED("Booking must be completed before rating."),
    
    // General
    TOO_MANY_REQUEST("Too many requests"),
    UNAUTHORIZED("Unauthorized"),
    UNKNOWN_ERROR("Unknown error.");

    String message;

}
