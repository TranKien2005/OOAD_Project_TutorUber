# TutorUber API Reference

## Base URL
```
http://localhost:8080/api
```

## Response Format

All API responses follow this format:

```json
{
  "success": true,
  "message": "Operation description",
  "data": { /* response data */ }
}
```

## Authentication

### Register User
**POST** `/auth/register`

Register a new user (Student, Tutor, or Admin).

**Request Body:**
```json
{
  "username": "student1",
  "password": "password123",
  "fullName": "Nguyen Van A",
  "email": "student1@example.com",
  "phone": "0987654321",
  "role": "STUDENT"  // STUDENT, TUTOR, or ADMIN
}
```

**Response:**
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "id": 2,
    "username": "student1",
    "fullName": "Nguyen Van A",
    "email": "student1@example.com",
    "phone": "0987654321",
    "role": "STUDENT",
    "active": true
  }
}
```

### Login
**POST** `/auth/login`

Login with username and password.

**Request Body:**
```json
{
  "username": "student1",
  "password": "password123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "simple-token-2",
    "user": {
      "id": 2,
      "username": "student1",
      "fullName": "Nguyen Van A",
      "email": "student1@example.com",
      "phone": "0987654321",
      "role": "STUDENT",
      "active": true
    }
  }
}
```

---

## Student Profile

### Get Student Profile
**GET** `/students/{studentId}/profile`

Get student profile information.

**Response:**
```json
{
  "success": true,
  "message": "Student profile retrieved successfully",
  "data": {
    "id": 1,
    "avatar": null,
    "preferenceSubjects": "Mathematics, Physics",
    "budgetMin": 150000.0,
    "budgetMax": 250000.0,
    "location": "District 1, HCMC",
    "bio": "Grade 12 student at Le Hong Phong High School"
  }
}
```

### Update Student Profile
**PUT** `/students/{studentId}/profile`

Update student profile.

**Request Body:**
```json
{
  "avatar": "https://example.com/avatar.jpg",
  "preferenceSubjects": "Mathematics, Physics, Chemistry",
  "budgetMin": 100000.0,
  "budgetMax": 300000.0,
  "location": "District 1, HCMC",
  "bio": "Grade 12 student preparing for university entrance exam"
}
```

**Response:** Same format as Get Student Profile

---

## Tutor Profile

### Get Tutor Profile
**GET** `/tutors/{tutorId}/profile`

Get tutor profile information.

**Response:**
```json
{
  "success": true,
  "message": "Tutor profile retrieved successfully",
  "data": {
    "id": 1,
    "avatar": null,
    "bio": "10 years of experience teaching Mathematics and Physics",
    "hourlyRate": 250000.0,
    "rating": 4.8,
    "totalRatings": 45,
    "verificationStatus": "APPROVED",
    "specialization": "Mathematics, Physics",
    "education": "PhD in Applied Mathematics - HCMUS",
    "yearsOfExperience": 10
  }
}
```

### Update Tutor Profile
**PUT** `/tutors/{tutorId}/profile`

Update tutor profile.

**Request Body:**
```json
{
  "avatar": "https://example.com/avatar.jpg",
  "bio": "10 years of experience teaching Mathematics",
  "hourlyRate": 280000.0,
  "specialization": "Mathematics, Physics",
  "education": "PhD in Applied Mathematics - HCMUS",
  "yearsOfExperience": 10
}
```

### Search Tutors
**POST** `/tutors/search`

Search for tutors based on criteria (all fields optional).

**Request Body:**
```json
{
  "subject": "Mathematics",
  "minRate": 100000.0,
  "maxRate": 300000.0,
  "minRating": 4.5,
  "verified": true
}
```

**Response:**
```json
{
  "success": true,
  "message": "Tutors retrieved successfully",
  "data": [
    {
      "id": 1,
      "avatar": null,
      "bio": "10 years of experience teaching Mathematics and Physics",
      "hourlyRate": 250000.0,
      "rating": 4.8,
      "totalRatings": 45,
      "verificationStatus": "APPROVED",
      "specialization": "Mathematics, Physics",
      "education": "PhD in Applied Mathematics - HCMUS",
      "yearsOfExperience": 10
    }
  ]
}
```

---

## Classes

### Create Class
**POST** `/classes`

Create a new class (tutor only).

**Request Body:**
```json
{
  "tutorId": 5,
  "subject": "Advanced Mathematics",
  "description": "Preparation for university entrance exam",
  "fee": 200000.0,
  "maxStudents": 10,
  "location": "Online via Zoom",
  "schedules": [
    {
      "dayOfWeek": "MONDAY",
      "startTime": "18:00",
      "endTime": "20:00"
    },
    {
      "dayOfWeek": "THURSDAY",
      "startTime": "18:00",
      "endTime": "20:00"
    }
  ]
}
```

**Response:**
```json
{
  "success": true,
  "message": "Class created successfully",
  "data": {
    "id": 1,
    "tutorId": 5,
    "tutorName": "Dr. Pham Van D",
    "subject": "Advanced Mathematics",
    "description": "Preparation for university entrance exam",
    "fee": 200000.0,
    "maxStudents": 10,
    "currentStudents": 0,
    "status": "ACTIVE",
    "location": "Online via Zoom",
    "schedules": [
      {
        "id": 1,
        "dayOfWeek": "MONDAY",
        "startTime": "18:00",
        "endTime": "20:00"
      }
    ]
  }
}
```

### Get Class Details
**GET** `/classes/{classId}`

Get detailed information about a class.

**Response:** Same format as Create Class response

### Update Class
**PUT** `/classes/{classId}`

Update class information (tutor only).

**Request Body:**
```json
{
  "subject": "Advanced Mathematics - Grade 12",
  "description": "Updated description",
  "fee": 220000.0,
  "maxStudents": 12,
  "location": "Online via Zoom",
  "status": "ACTIVE",
  "schedules": [
    {
      "dayOfWeek": "MONDAY",
      "startTime": "18:00",
      "endTime": "20:00"
    }
  ]
}
```

### Delete Class
**DELETE** `/classes/{classId}`

Delete a class (tutor only).

**Response:**
```json
{
  "success": true,
  "message": "Class deleted successfully",
  "data": null
}
```

### Get Tutor's Classes
**GET** `/classes/tutor/{tutorId}`

Get all classes created by a tutor.

**Response:**
```json
{
  "success": true,
  "message": "Classes retrieved successfully",
  "data": [
    {
      "id": 1,
      "tutorId": 5,
      "tutorName": "Dr. Pham Van D",
      "subject": "Mathematics",
      "description": "...",
      "fee": 200000.0,
      "maxStudents": 10,
      "currentStudents": 2,
      "status": "ACTIVE",
      "schedules": [...]
    }
  ]
}
```

---

## Bookings

### Create Booking
**POST** `/bookings`

Create a new booking (student enrolls in a class).

**Request Body:**
```json
{
  "studentId": 2,
  "classId": 1
}
```

**Response:**
```json
{
  "success": true,
  "message": "Booking created successfully",
  "data": {
    "id": 1,
    "studentId": 2,
    "studentName": "Nguyen Van A",
    "classId": 1,
    "className": "Mathematics",
    "status": "PENDING",
    "totalPrice": 200000.0,
    "bookedDate": "2025-11-22T10:30:00",
    "confirmedAt": null,
    "cancelledAt": null,
    "cancellationReason": null
  }
}
```

### Get Booking Details
**GET** `/bookings/{bookingId}`

Get detailed information about a booking.

**Response:** Same format as Create Booking response

### Confirm Booking
**PUT** `/bookings/{bookingId}/confirm`

Confirm a booking (tutor accepts student).

**Response:**
```json
{
  "success": true,
  "message": "Booking confirmed successfully",
  "data": {
    "id": 1,
    "status": "CONFIRMED",
    "confirmedAt": "2025-11-22T11:00:00",
    ...
  }
}
```

### Cancel Booking
**PUT** `/bookings/{bookingId}/cancel`

Cancel a booking (student cancels enrollment).

**Request Body (optional):**
```json
{
  "cancellationReason": "Schedule conflict"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Booking cancelled successfully",
  "data": {
    "id": 1,
    "status": "CANCELLED",
    "cancelledAt": "2025-11-22T12:00:00",
    "cancellationReason": "Schedule conflict",
    ...
  }
}
```

### Get Student's Bookings
**GET** `/bookings/student/{studentId}`

Get all bookings for a student.

**Response:**
```json
{
  "success": true,
  "message": "Bookings retrieved successfully",
  "data": [
    {
      "id": 1,
      "studentId": 2,
      "classId": 1,
      "status": "CONFIRMED",
      ...
    }
  ]
}
```

### Get Class Bookings
**GET** `/bookings/class/{classId}`

Get all bookings for a class.

**Response:** Same format as Get Student's Bookings

---

## Ratings

### Submit Rating
**POST** `/ratings`

Submit a rating for a tutor (student only, must have booking).

**Request Body:**
```json
{
  "studentId": 2,
  "tutorId": 5,
  "bookingId": 1,
  "score": 5,
  "comment": "Excellent teacher! Very clear explanations."
}
```

**Response:**
```json
{
  "success": true,
  "message": "Rating submitted successfully",
  "data": {
    "id": 1,
    "studentId": 2,
    "studentName": "Nguyen Van A",
    "tutorId": 5,
    "score": 5,
    "comment": "Excellent teacher! Very clear explanations.",
    "createdAt": "2025-11-22T15:00:00"
  }
}
```

### Get Tutor's Ratings
**GET** `/ratings/tutor/{tutorId}`

Get all ratings for a tutor.

**Response:**
```json
{
  "success": true,
  "message": "Ratings retrieved successfully",
  "data": [
    {
      "id": 1,
      "studentId": 2,
      "studentName": "Nguyen Van A",
      "tutorId": 5,
      "score": 5,
      "comment": "Excellent teacher!",
      "createdAt": "2025-11-22T15:00:00"
    }
  ]
}
```

---

## Enums

### User Roles
- `STUDENT`
- `TUTOR`
- `ADMIN`

### Booking Status
- `PENDING` - Waiting for tutor confirmation
- `CONFIRMED` - Tutor accepted
- `CANCELLED` - Student cancelled
- `COMPLETED` - Class finished

### Class Status
- `ACTIVE` - Class is open for enrollment
- `FULL` - Class reached max students
- `CLOSED` - Class no longer accepting students
- `CANCELLED` - Class cancelled by tutor

### Verification Status (Tutor)
- `PENDING` - Awaiting verification
- `APPROVED` - Verified tutor
- `REJECTED` - Verification rejected

---

## Sample Test Accounts

### Students
- Username: `student1` / `student2` / `student3`
- Password: `password123`

### Tutors
- Username: `tutor1` / `tutor2` / `tutor3`
- Password: `password123`

### Admin
- Username: `admin`
- Password: `admin123`

---

## Error Responses

When an error occurs, the response format is:

```json
{
  "success": false,
  "message": "Error description",
  "data": null
}
```

### Common Error Codes

**400 Bad Request** - Invalid input data
```json
{
  "success": false,
  "message": "Validation failed",
  "data": null
}
```

**404 Not Found** - Resource not found
```json
{
  "success": false,
  "message": "Student not found",
  "data": null
}
```

**409 Conflict** - Business logic violation
```json
{
  "success": false,
  "message": "Class is full",
  "data": null
}
```

---

## Notes for Frontend Developers

1. **No Authentication Required**: This is a simplified demo version. All endpoints are publicly accessible.

2. **CORS**: Enabled for all origins (`*`). No need to configure CORS on frontend.

3. **Date/Time Format**: All timestamps use ISO 8601 format: `2025-11-22T15:00:00`

4. **Money Format**: All prices are in VND as Double (e.g., `200000.0`)

5. **Context Path**: All endpoints must include `/api` prefix

6. **Response Structure**: Always check `success` field before processing `data`

7. **Sample Data**: Database is pre-populated with sample data on first run

8. **Docker**: Backend runs in Docker. Start with `docker-compose up -d`

9. **Hot Reload**: Changes require rebuild: `docker-compose up -d --build`

10. **Database Reset**: Use `docker-compose down -v` to reset all data
